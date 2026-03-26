package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UnionFind<T> implements Printable, Destructible {
	
	private final HashMap<T, UnionFindNode<T>> nodes;
	
	public UnionFind() {
		nodes = new HashMap<>();
	}
	
	public UnionFind(int initialCapacity) {
		nodes = new HashMap<>(initialCapacity);
	}
	
	public UnionFind(Collection<T> values) {
		this(Checks.isNotNull(values, "values").size());
		for (T value : values) {
			add(value);
		}
	}
	
	public UnionFindNode<T> add(T value) {
		Checks.isNotNull(value, "value");
		return nodes.putIfAbsent(value, new UnionFindNode<>(value));
	}
	
	public UnionFindNode<T> find(T value) {
		UnionFindNode<T> node = getNode(value);
		if (node == null) {
			return null;
		}
		return node.find();
	}
	
	public boolean union(T valueA, T valueB) {
		UnionFindNode<T> nodeA = getNode(valueA);
		UnionFindNode<T> nodeB = getNode(valueB);
		if (nodeA == null || nodeB == null) {
			return false;
		}
		return nodeA.union(nodeB);
	}
	
	public boolean isConnected(T valueA, T valueB) {
		UnionFindNode<T> nodeA = getNode(valueA);
		UnionFindNode<T> nodeB = getNode(valueB);
		if (nodeA == null || nodeB == null) {
			return false;
		}
		return nodeA.find().equals(nodeB.find());
	}
	
	public UnionFindNode<T> getNode(T value) {
		return nodes.get(value);
	}
	
	public boolean contains(T value) {
		return nodes.containsKey(value);
	}
	
	public int size() {
		return nodes.size();
	}
	
	public Collection<UnionFindNode<T>> nodes() {
		return nodes.values();
	}
	
	public Set<T> keys() {
		return nodes.keySet();
	}
	
	public Set<UnionFindNode<T>> getRoots() {
		Set<UnionFindNode<T>> roots = new HashSet<>();
		for (UnionFindNode<T> node : nodes.values()) {
			roots.add(node.find());
		}
		return roots;
	}
	
	@Override
	public @NotNull String toString() {
		return Logger.formatCollection(nodes());
	}
	
	@Override
	public void destroy() {
		nodes.clear();
	}
	
}