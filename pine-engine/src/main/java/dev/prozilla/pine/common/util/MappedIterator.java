package dev.prozilla.pine.common.util;

import dev.prozilla.pine.common.util.function.mapper.Mapper;

import java.util.Iterator;

public class MappedIterator<A, B> implements Iterator<B> {
	
	private final Iterator<A> iterator;
	private final Mapper<A, B> mapper;
	
	public MappedIterator(Iterator<A> iterator, Mapper<A, B> mapper) {
		this.iterator = iterator;
		this.mapper = mapper;
	}
	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
	@Override
	public B next() {
		return mapper.map(iterator.next());
	}
	
	@Override
	public void remove() {
		iterator.remove();
	}
	
}
