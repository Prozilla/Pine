package dev.prozilla.pine.examples.snake.system;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.sprite.TileRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;
import dev.prozilla.pine.examples.snake.GameScene;
import dev.prozilla.pine.examples.snake.component.PlayerData;
import dev.prozilla.pine.examples.snake.component.PlayerTailData;

public class PlayerTailRenderer extends RenderSystem {
	
	public PlayerTailRenderer() {
		super(PlayerTailData.class, TileRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		PlayerTailData tailData = chunk.getComponent(PlayerTailData.class);
		if (!tailData.isCurved) {
			return;
		}
		
		TileRenderer tile = chunk.getComponent(TileRenderer.class);
		renderGap(renderer, tailData, tile, tailData.previousTile, tailData.nextTile);
		renderGap(renderer, tailData, tile, tailData.nextTile, tailData.previousTile);
	}
	
	private void renderGap(Renderer renderer, PlayerTailData tailData, TileRenderer tile, TileRenderer neighbor, TileRenderer otherNeighbor) {
		if (neighbor == null) {
			return;
		}
		
		Vector2i direction = neighbor.getCoordinate().clone().subtract(tile.getCoordinate());
		float gap = getGap(tailData.playerData, neighbor, direction);
		if (gap <= 0) {
			return;
		}
		
		TextureAsset texture = AssetPools.textures.load("snake/snake_body_straight.png");
		int size = tile.size;
		Vector2i otherDirection = otherNeighbor == null
			? new Vector2i()
			: otherNeighbor.getCoordinate().clone().subtract(tile.getCoordinate());
		boolean neighborIsHead = neighbor.getComponent(PlayerTailData.class) == null;
		float farOverlap = neighborIsHead ? 1f : 0.5f;
		
		float thickness = 31f;
		float crossStart = (size - thickness) / 2f;
		
		if (direction.x != 0) {
			float width = gap + 0.5f + farOverlap;
			float x = direction.x > 0 ? size - 0.5f : -gap - farOverlap;
			float y = crossStart + otherDirection.y * 0.5f;
			float s1 = 0.5f / texture.getWidth();
			float s2 = (width + 0.5f) / texture.getWidth();
			float t1 = (size + 5.5f) / texture.getHeight();
			float t2 = (size + 34.5f) / texture.getHeight();
			renderer.drawQuad(texture, x, y, 0, x + width, y + thickness, 0, s1, t1, s2, t2);
		} else {
			float height = gap + 0.5f + farOverlap;
			float x = crossStart + otherDirection.x * 0.5f;
			float y = direction.y > 0 ? size - 0.5f : -gap - farOverlap;
			float s1 = 5.5f / texture.getWidth();
			float s2 = 34.5f / texture.getWidth();
			float t1 = 0.5f / texture.getHeight();
			float t2 = (height + 0.5f) / texture.getHeight();
			renderer.drawQuad(texture, x, y, 0, x + thickness, y + height, 0, s1, t1, s2, t2);
		}
	}
	
	private float getGap(PlayerData playerData, TileRenderer neighbor, Vector2i direction) {
		PlayerTailData neighborData = neighbor.getComponent(PlayerTailData.class);
		if (neighborData != null && neighborData.isCurved) {
			return 0;
		}
		
		float delta = (0.5f - playerData.timeUntilNextMove / PlayerData.TIME_BETWEEN_MOVES) * GameScene.CELL_SIZE;
		Vector2i offsetDirection;
		if (neighborData == null) {
			offsetDirection = getDirection(playerData.direction);
		} else {
			offsetDirection = neighborData.previousTile.getCoordinate().clone().subtract(neighbor.getCoordinate());
		}
		
		return Math.max(0, delta * offsetDirection.dot(direction));
	}
	
	private Vector2i getDirection(int direction) {
		return switch (direction) {
			case 0 -> new Vector2i(0, 1);
			case 1 -> new Vector2i(-1, 0);
			case 2 -> new Vector2i(0, -1);
			default -> new Vector2i(1, 0);
		};
	}
}
