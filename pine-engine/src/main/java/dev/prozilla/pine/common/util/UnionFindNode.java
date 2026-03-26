package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UnionFindNode<T> implements Printable, Cloneable<UnionFindNode<T>> {
	
	private final T value;
	private UnionFindNode<T> next;
	private int rank;
	
	public UnionFindNode(T value) {
		this.value = value;
		
		next = this;
		rank = 0;
	}
	
	public T getValue() {
		return value;
	}
	
	public UnionFindNode<T> getNext() {
		return next;
	}
	
	public UnionFindNode<T> find() {
		UnionFindNode<T> node = this;
		while (!node.equals(node.next)) {
			node = node.next;
		}
		return node;
	}
	
	public void join(UnionFindNode<T> other) {
		Checks.isNotNull(other, "other");
		other.next = this;
	}
	
	public boolean union(UnionFindNode<T> other) {
		UnionFindNode<T> nodeA = find();
		UnionFindNode<T> nodeB = other.find();
		if (nodeA.equals(nodeB)) {
			return false;
		}
		if (nodeA.rank < nodeB.rank){
			nodeB.join(nodeA);
			if (nodeA.rank == nodeB.rank) {
				nodeB.rank++;
			}
		} else {
			nodeA.join(nodeB);
			if (nodeA.rank == nodeB.rank) {
				nodeA.rank++;
			}
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		return other == this || (other instanceof UnionFindNode<?> otherNode && equals(otherNode));
	}
	
	@Override
	public boolean equals(UnionFindNode<T> other) {
		return other != null && Objects.equals(value, other.value);
	}
	
	@Override
	public UnionFindNode<T> clone() {
		return new UnionFindNode<>(value);
	}
	
	@Override
	public @NotNull String toString() {
		StringBuilder stringBuilder = new StringBuilder(String.format("(%s)", value));
		if (!next.equals(this)) {
			stringBuilder.append("->");
			stringBuilder.append(next.toString());
		}
		return stringBuilder.toString();
	}
	
}