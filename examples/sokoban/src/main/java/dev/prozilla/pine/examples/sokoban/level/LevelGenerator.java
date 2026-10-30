package dev.prozilla.pine.examples.sokoban.level;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2i;

import java.util.*;

public class LevelGenerator {
	
	public static final int MIN_SIZE = 7;
	public static final int MAX_ATTEMPTS = 1000;
	
	private static final int WALL_COST = 100;
	private static final int CRATE_COST = 10000;
	private static final int PATH_COST = 1;
	private static final int PLAYER_PATH_COST = -1;
	private static final int MAX_STEPS = 4000;
	
	private static final int BLOCK_TYPES = 4;
	private static final int CRATE_TYPES = 6;
	private static final int GROUND_TYPES = 3;
	
	private final Random random;
	
	public LevelGenerator() {
		this(new Random());
	}
	
	public LevelGenerator(long seed) {
		this(new Random(seed));
	}
	
	public LevelGenerator(Random random) {
		this.random = random;
	}
	
	public Level generate() {
		for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
			int size;
			int crateCount;
			double roll = random.nextDouble();
			
			if (roll <= 0.1) {
				size = randomInt(7, 9);
				crateCount = randomInt(2, 4);
			} else if (roll <= 0.3) {
				size = randomInt(8, 12);
				crateCount = 3;
			} else if (roll <= 0.7) {
				size = randomInt(9, 13);
				crateCount = 4;
			} else if (roll <= 0.9) {
				size = randomInt(9, 14);
				crateCount = 5;
			} else if (roll <= 0.96) {
				size = randomInt(16, 20);
				crateCount = randomInt(4, 8);
			} else {
				size = randomInt(9, 16);
				crateCount = randomInt(6, 14);
			}
			
			Level level = attemptGeneration(size, size, crateCount);
			if (level != null) {
				return level;
			}
		}
		
		throw new IllegalStateException("Failed to generate a level after " + MAX_ATTEMPTS + " attempts");
	}
	
	public Level generate(int size, int crateCount) {
		return generate(size, size, crateCount);
	}
	
	public Level generate(int width, int height, int crateCount) {
		width = Math.max(width, MIN_SIZE);
		height = Math.max(height, MIN_SIZE);
		
		// Objects are never placed in the outer 2 layers
		int maxCrateCount = ((width - 4) * (height - 4) - 2) / 2;
		crateCount = MathUtils.clamp(crateCount, 1, maxCrateCount);
		
		for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
			Level level = attemptGeneration(width, height, crateCount);
			if (level != null) {
				return level;
			}
		}
		
		throw new IllegalStateException("Failed to generate a level after " + MAX_ATTEMPTS + " attempts");
	}
	
	private Level attemptGeneration(int width, int height, int crateCount) {
		Board board = new Board(width, height, crateCount);
		if (!board.placed) {
			return null;
		}
		
		board.removeRandomWalls(randomInt(-2, 5));
		return generatePaths(board) ? board.toLevel() : null;
	}
	
	/**
	 * Solves the level with ghost crates and removes all walls along the way.
	 * @return {@code false} if the level turned out to be invalid and has to be thrown away.
	 */
	private boolean generatePaths(Board board) {
		int steps = 0;
		
		List<Crate> ghostCrates = board.copyCrates();
		
		// Push the ghost crates towards their goals
		while (board.solveCounter > 0) {
			// Calculate the paths from all crates to their goals
			List<Path> cratePaths = calculateCratePaths(board, ghostCrates);
			if (cratePaths == null) {
				return false;
			}
			
			// Calculate the player paths to all crates and choose the lowest cost path
			Path[] playerPaths = new Path[ghostCrates.size()];
			int bestIndex = -1;
			int minCost = 100_000_000;
			
			for (int i = 0; i < ghostCrates.size(); i++) {
				Crate crate = ghostCrates.get(i);
				Node firstStep = cratePaths.get(i).nodes().getFirst();
				
				Pathfinder pathfinder = createPathFinder(board, crate, firstStep);
				playerPaths[i] = pathfinder.findPath(false);
				
				if (playerPaths[i] != null && playerPaths[i].cost() < minCost) {
					minCost = playerPaths[i].cost();
					bestIndex = i;
				}
			}
			
			if (bestIndex == -1) {
				return false;
			}
			
			List<Node> playerPath = playerPaths[bestIndex].nodes();
			List<Node> cratePath = cratePaths.get(bestIndex).nodes();
			
			// Remove all walls on the player's path
			for (Node node : playerPath) {
				if (node.occupied) {
					return false;
				}
				node.wall = false;
			}
			
			// Push the crate into the solving direction
			Crate crate = ghostCrates.get(bestIndex);
			Node currentNode = cratePath.getFirst();
			int deltaX = currentNode.x - crate.x;
			int deltaY = currentNode.y - crate.y;
			
			// If the crate path is longer than 1, push until there is a turn.
			int stop = 0;
			for (int i = 1; i < cratePath.size(); i++) {
				Node nextNode = cratePath.get(i);
				if (deltaX == nextNode.x - currentNode.x && deltaY == nextNode.y - currentNode.y) {
					currentNode = nextNode;
				} else {
					stop = i - 1;
					break;
				}
			}
			
			// Remove all walls on the crate's path
			for (int i = 0; i <= stop; i++) {
				Node node = cratePath.get(i);
				if (node.occupied) {
					return false;
				}
				node.wall = false;
			}
			
			// Set new player and crate positions
			board.nodes[crate.x][crate.y].occupied = false;
			crate.x = cratePath.get(stop).x;
			crate.y = cratePath.get(stop).y;
			board.nodes[crate.x][crate.y].occupied = true;
			board.playerX = crate.x - deltaX;
			board.playerY = crate.y - deltaY;
			
			// Check if the moved crate is on its goal
			if (crate.x == crate.goal.x && crate.y == crate.goal.y) {
				board.solveCounter--;
				ghostCrates.remove(bestIndex);
			}
			
			steps++;
			if (steps > MAX_STEPS) {
				return false;
			}
		}
		
		// Reset player position
		board.playerX = board.spawnX;
		board.playerY = board.spawnY;
		
		return true;
	}
	
	/**
	 * Calculates the path of every crate to its goal.
	 * @return A list of paths in the same order as the crates, or {@code null} if a path could not be found
	 */
	private List<Path> calculateCratePaths(Board board, List<Crate> crates) {
		List<Path> paths = new ArrayList<>(crates.size());
		
		for (Crate crate : crates) {
			// The crate itself should not block its own path
			board.nodes[crate.x][crate.y].occupied = false;
			Pathfinder pathfinder = new Pathfinder(board, crate.x, crate.y, crate.goal.x, crate.goal.y);
			Path path = pathfinder.findPath(true);
			board.nodes[crate.x][crate.y].occupied = true;
			
			if (path == null || path.nodes().isEmpty()) {
				return null;
			}
			paths.add(path);
		}
		
		return paths;
	}
	
	private int randomInt(int max) {
		return randomInt(0, max);
	}
	
	private int randomInt(int min, int max) {
		return max > min ? min + random.nextInt(max - min) : min;
	}
	
	private static Pathfinder createPathFinder(Board board, Crate crate, Node firstStep) {
		// The player has to stand on the opposite side of the first push direction
		int playerX = crate.x;
		int playerY = crate.y;
		if (firstStep.x == crate.x + 1) {
			playerX -= 1;
		} else if (firstStep.x == crate.x - 1) {
			playerX += 1;
		} else if (firstStep.y == crate.y + 1) {
			playerY -= 1;
		} else {
			playerY += 1;
		}
		
		return new Pathfinder(board, board.playerX, board.playerY, playerX, playerY);
	}
	
	private class Board {
		
		public final int width;
		public final int height;
		public final Node[][] nodes;
		public final List<Node> goals = new ArrayList<>();
		public final List<Crate> crates = new ArrayList<>();
		
		public final List<Node> availableNodes = new ArrayList<>();
		
		public int spawnX;
		public int spawnY;
		public int playerX;
		public int playerY;
		public int solveCounter;
		
		public boolean placed;
		
		Board(int width, int height, int crateCount) {
			this.width = width;
			this.height = height;
			this.nodes = new Node[width][height];
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					nodes[x][y] = new Node(x, y);
				}
			}
			
			// Objects are never placed in the outer 2 layers
			for (int x = 2; x < width - 2; x++) {
				availableNodes.addAll(Arrays.asList(nodes[x]).subList(2, height - 2));
			}
			
			placed = placeObjects(crateCount);
			solveCounter = crates.size();
		}
		
		private boolean placeObjects(int crateCount) {
			for (int i = 0; i < crateCount; i++) {
				Node node = useAvailableNode();
				if (node == null) {
					return false;
				}
				goals.add(node);
			}
			
			for (int i = 0; i < crateCount; i++) {
				Node node = useAvailableNode();
				if (node == null) {
					return false;
				}
				crates.add(new Crate(node.x, node.y, goals.get(i)));
				node.hasCrate = true;
			}
			
			Node playerNode = useAvailableNode();
			if (playerNode == null) {
				playerNode = goals.getFirst();
			}
			spawnX = playerX = playerNode.x;
			spawnY = playerY = playerNode.y;
			return true;
		}
		
		void removeRandomWalls(int amount) {
			for (int i = 0; i < amount; i++) {
				if (!availableNodes.isEmpty()) {
					useAvailableNode();
				}
			}
		}
		
		private Node useAvailableNode() {
			while (!availableNodes.isEmpty()) {
				Node node = availableNodes.remove(random.nextInt(availableNodes.size()));
				node.wall = false;
				if (!isBoxedIn(node.x, node.y)) {
					return node;
				}
			}
			return null;
		}
		
		private boolean isBoxedIn(int x, int y) {
			return (nodes[x + 1][y].hasCrate && (
				(nodes[x + 1][y + 1].hasCrate && nodes[x][y + 1].hasCrate)
					|| (nodes[x + 1][y - 1].hasCrate && nodes[x][y - 1].hasCrate)))
				       || (nodes[x - 1][y].hasCrate && (
				(nodes[x - 1][y - 1].hasCrate && nodes[x][y - 1].hasCrate)
					|| (nodes[x - 1][y + 1].hasCrate && nodes[x][y + 1].hasCrate)));
		}
		
		private List<Crate> copyCrates() {
			List<Crate> copies = new ArrayList<>(crates.size());
			for (Crate crate : crates) {
				copies.add(new Crate(crate.x, crate.y, crate.goal));
				nodes[crate.x][crate.y].occupied = true;
			}
			return copies;
		}
		
		private boolean contains(int x, int y) {
			return x >= 0 && x < width && y >= 0 && y < height;
		}
		
		/**
		 * Converts the board to a level. Only the area that the player can reach is kept, together with
		 * a wall that is 1 tile thin around it (corners included). The level is cropped to fit this area.
		 */
		private Level toLevel() {
			boolean[][] floor = findFloor();
			
			// Find the bounds of the floor
			int minX = width;
			int minY = height;
			int maxX = -1;
			int maxY = -1;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (floor[x][y]) {
						minX = Math.min(minX, x);
						minY = Math.min(minY, y);
						maxX = Math.max(maxX, x);
						maxY = Math.max(maxY, y);
					}
				}
			}
			
			// The level is the floor plus a wall of 1 tile around it
			int levelWidth = maxX - minX + 3;
			int levelHeight = maxY - minY + 3;
			int offsetX = 1 - minX;
			int offsetY = 1 - minY;
			
			// Place walls on every tile that is next to a floor tile, including diagonally
			boolean[][] wall = new boolean[levelWidth][levelHeight];
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					if (!floor[x][y]) {
						continue;
					}
					
					for (int deltaX = -1; deltaX <= 1; deltaX++) {
						for (int deltaY = -1; deltaY <= 1; deltaY++) {
							int neighborX = x + deltaX;
							int neighborY = y + deltaY;
							if (!contains(neighborX, neighborY) || !floor[neighborX][neighborY]) {
								wall[neighborX + offsetX][neighborY + offsetY] = true;
							}
						}
					}
				}
			}
			
			List<Vector2i> walls = new ArrayList<>();
			for (int y = 0; y < levelHeight; y++) {
				for (int x = 0; x < levelWidth; x++) {
					if (wall[x][y]) {
						walls.add(new Vector2i(x, y));
					}
				}
			}
			
			List<Vector2i> goals = new ArrayList<>(this.goals.size());
			for (Node goal : this.goals) {
				goals.add(new Vector2i(goal.x + offsetX, goal.y + offsetY));
			}
			
			List<Vector2i> crates = new ArrayList<>(this.crates.size());
			for (Crate crate : this.crates) {
				crates.add(new Vector2i(crate.x + offsetX, crate.y + offsetY));
			}
			
			int blockType, crateType, groundType;
			do {
				blockType = randomInt(BLOCK_TYPES);
				crateType = randomInt(CRATE_TYPES);
				groundType = randomInt(GROUND_TYPES);
			} while (!isValidTypeCombination(blockType, crateType, groundType));
			
			return new Level(new Vector2i(levelWidth, levelHeight), walls, goals, crates, new Vector2i(spawnX + offsetX, spawnY + offsetY), blockType, crateType, groundType);
		}
		
		/**
		 * Finds all floor tiles that the player can reach from the spawn.
		 * Floor tiles that are not connected to the spawn are discarded.
		 */
		private boolean[][] findFloor() {
			boolean[][] floor = new boolean[width][height];
			Deque<Node> queue = new ArrayDeque<>();
			
			floor[spawnX][spawnY] = true;
			queue.add(nodes[spawnX][spawnY]);
			
			int[] deltaX = {1, -1, 0, 0};
			int[] deltaY = {0, 0, 1, -1};
			
			while (!queue.isEmpty()) {
				Node node = queue.remove();
				for (int i = 0; i < 4; i++) {
					int x = node.x + deltaX[i];
					int y = node.y + deltaY[i];
					if (contains(x, y) && !floor[x][y] && !nodes[x][y].wall) {
						floor[x][y] = true;
						queue.add(nodes[x][y]);
					}
				}
			}
			
			return floor;
		}
		
		private static boolean isValidTypeCombination(int blockType, int crateType, int groundType) {
			return true;
		}
		
	}
	
	private static class Node {
		
		public final int x;
		public final int y;
		
		// Pathfinding state
		public int estimatedCost;
		public int cost;
		public boolean closed;
		public boolean checked;
		public Node parent;
		
		public boolean wall;
		public boolean occupied;
		public boolean hasCrate;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
			
			wall = true;
		}
		
	}
	
	private static class Crate {
		
		public int x;
		public int y;
		public final Node goal;
		
		public Crate(int x, int y, Node goal) {
			this.x = x;
			this.y = y;
			this.goal = goal;
		}
		
	}
	
	private static class Pathfinder {
		
		private final Board board;
		private final Node[][] nodes;
		private final int startX;
		private final int startY;
		private final int endX;
		private final int endY;
		private final List<Node> openNodes = new ArrayList<>();
		private final List<Node> closedNodes = new ArrayList<>();
		
		public Pathfinder(Board board, int startX, int startY, int endX, int endY) {
			this.board = board;
			this.nodes = board.nodes;
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
		}
		
		public Path findPath(boolean isCrate) {
			if (!board.contains(startX, startY) || !board.contains(endX, endY)) {
				return null;
			}
			
			openNodes.add(nodes[startX][startY]);
			
			while (!openNodes.isEmpty()) {
				Node current = openNodes.removeFirst();
				
				if (current.x == endX && current.y == endY) {
					openNodes.add(current);
					return buildPath(current);
				}
				
				current.closed = true;
				closedNodes.add(current);
				
				checkNeighbor(current.x + 1, current.y, current, isCrate);
				checkNeighbor(current.x - 1, current.y, current, isCrate);
				checkNeighbor(current.x, current.y + 1, current, isCrate);
				checkNeighbor(current.x, current.y - 1, current, isCrate);
			}
			
			resetNodes();
			return null;
		}
		
		private void checkNeighbor(int x, int y, Node parent, boolean isCrate) {
			if (!board.contains(x, y)) {
				return;
			}
			
			Node node = nodes[x][y];
			
			if (!node.closed && !node.checked) {
				node.cost = calculateCost(node, parent, isCrate);
				node.estimatedCost = node.cost + Math.abs(x - endX) + Math.abs(y - endY);
				node.parent = parent;
				node.checked = true;
				addToSortedList(openNodes, node);
			} else if (!node.closed) {
				int cost = calculateCost(node, parent, isCrate);
				if (cost < node.cost && node.parent.parent != null) {
					node.cost = cost;
					node.estimatedCost = node.cost + Math.abs(x - endX) + Math.abs(y - endY);
					node.parent = parent;
				}
			}
		}
		
		private int calculateCost(Node node, Node parent, boolean isCrate) {
			int cost;
			
			if (node.occupied) {
				cost = parent.cost + CRATE_COST;
			} else if (isCrate) {
				cost = parent.cost + (node.wall ? WALL_COST : PATH_COST);
			} else {
				cost = parent.cost + (node.wall ? WALL_COST : PLAYER_PATH_COST);
			}
			
			if (isCrate && parent.parent != null) {
				int x = node.x;
				int y = node.y;
				int cost1 = 0;
				int cost2 = 0;
				
				if (x - 1 == parent.x && x - 2 != parent.parent.x) {
					if (y - 1 == parent.parent.y) {
						cost1 = nodeCost(x - 2, y) + nodeCost(x - 2, y - 1);
						cost2 = nodeCost(x, y - 1) + nodeCost(x, y + 1) + nodeCost(x - 1, y + 1) + nodeCost(x - 2, y + 1) + nodeCost(x - 2, y);
					} else {
						cost1 = nodeCost(x - 2, y) + nodeCost(x - 2, y + 1);
						cost2 = nodeCost(x, y - 1) + nodeCost(x, y + 1) + nodeCost(x - 1, y - 1) + nodeCost(x - 2, y - 1) + nodeCost(x - 2, y);
					}
				} else if (x + 1 == parent.x && x + 2 != parent.parent.x) {
					if (y - 1 == parent.parent.y) {
						cost1 = nodeCost(x + 2, y) + nodeCost(x + 2, y - 1);
						cost2 = nodeCost(x, y - 1) + nodeCost(x, y + 1) + nodeCost(x + 1, y + 1) + nodeCost(x + 2, y + 1) + nodeCost(x + 2, y);
					} else {
						cost1 = nodeCost(x + 2, y) + nodeCost(x + 2, y + 1);
						cost2 = nodeCost(x, y - 1) + nodeCost(x, y + 1) + nodeCost(x + 1, y - 1) + nodeCost(x + 2, y - 1) + nodeCost(x + 2, y);
					}
				} else if (y - 1 == parent.y && y - 2 != parent.parent.y) {
					if (x - 1 == parent.parent.x) {
						cost1 = nodeCost(x, y - 2) + nodeCost(x - 1, y - 2);
						cost2 = nodeCost(x - 1, y) + nodeCost(x + 1, y) + nodeCost(x + 1, y - 1) + nodeCost(x + 1, y - 2) + nodeCost(x, y - 2);
					} else {
						cost1 = nodeCost(x, y - 2) + nodeCost(x + 1, y - 2);
						cost2 = nodeCost(x - 1, y) + nodeCost(x + 1, y) + nodeCost(x - 1, y - 1) + nodeCost(x - 1, y - 2) + nodeCost(x, y - 2);
					}
				} else if (y + 1 == parent.y && y + 2 != parent.parent.y) {
					if (x - 1 == parent.parent.x) {
						cost1 = nodeCost(x, y + 2) + nodeCost(x - 1, y + 2);
						cost2 = nodeCost(x - 1, y) + nodeCost(x + 1, y) + nodeCost(x + 1, y + 1) + nodeCost(x + 1, y + 2) + nodeCost(x, y + 2);
					} else {
						cost1 = nodeCost(x, y + 2) + nodeCost(x + 1, y + 2);
						cost2 = nodeCost(x - 1, y) + nodeCost(x + 1, y) + nodeCost(x - 1, y + 1) + nodeCost(x - 1, y + 2) + nodeCost(x, y + 2);
					}
				}
				
				cost += Math.min(cost1, cost2);
			} else if (isCrate) {
				if (node.x - 1 == parent.x) {
					cost += nodeCost(node.x - 2, node.y);
				} else if (node.x + 1 == parent.x) {
					cost += nodeCost(node.x + 2, node.y);
				} else if (node.y - 1 == parent.y) {
					cost += nodeCost(node.x, node.y - 2);
				} else if (node.y + 1 == parent.y) {
					cost += nodeCost(node.x, node.y + 2);
				}
			}
			
			return cost;
		}
		
		private int nodeCost(int x, int y) {
			if (!board.contains(x, y)) {
				return CRATE_COST;
			}
			
			Node node = nodes[x][y];
			if (node.occupied) {
				return CRATE_COST;
			}
			return node.wall ? WALL_COST : PLAYER_PATH_COST;
		}
		
		private Path buildPath(Node endNode) {
			int cost = endNode.cost;
			List<Node> path = new ArrayList<>();
			
			Node node = endNode;
			while (node.parent != null) {
				path.add(node);
				node = node.parent;
			}
			Collections.reverse(path);
			
			resetNodes();
			return new Path(path, cost);
		}
		
		private void resetNodes() {
			for (Node node : openNodes) {
				resetNode(node);
			}
			for (Node node : closedNodes) {
				resetNode(node);
			}
		}
		
		private static void addToSortedList(List<Node> list, Node element) {
			int index = binarySearch(list, element);
			if (index < 0) {
				index = -index - 1;
			}
			list.add(index, element);
		}
		
		private static int binarySearch(List<Node> list, Node element) {
			int lower = 0;
			int upper = list.size() - 1;
			
			while (lower <= upper) {
				int center = (upper + lower) >> 1;
				int costDifference = element.estimatedCost - list.get(center).estimatedCost;
				if (costDifference > 0) {
					lower = center + 1;
				} else if (costDifference < 0) {
					upper = center - 1;
				} else {
					return center;
				}
			}
			
			return -lower - 1;
		}
		
		private static void resetNode(Node node) {
			node.checked = false;
			node.closed = false;
			node.parent = null;
			node.cost = 0;
		}
		
	}
	
	private record Path(List<Node> nodes, int cost) {}
	
}