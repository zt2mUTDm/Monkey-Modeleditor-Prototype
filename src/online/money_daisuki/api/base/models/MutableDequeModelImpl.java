package online.money_daisuki.api.base.models;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class MutableDequeModelImpl<E> implements MutableDequeModel<E> {
	private final Deque<E> parent;
	private final List<DataSink<? super MutableDequeModel<E>>> listeners;
	
	public MutableDequeModelImpl() {
		parent = new LinkedList<>();
		listeners = new LinkedList<>();
	}
	
	@Override
	public void addFirst(final E e) {
		parent.addFirst(Requires.notNull(e, "e == null"));
		fireChangeListener();
	}
	@Override
	public void addLast(final E e) {
		parent.addLast(Requires.notNull(e, "e == null"));
		fireChangeListener();
	}
	
	@Override
	public E getFirst() {
		return(parent.getFirst());
	}
	@Override
	public E getLast() {
		return(parent.getLast());
	}
	
	@Override
	public E removeFirst() {
		final E value = parent.removeFirst();
		if(value != null) {
			fireChangeListener();
		}
		return(value);
	}
	@Override
	public E removeLast() {
		final E value = parent.removeLast();
		if(value != null) {
			fireChangeListener();
		}
		return(value);
	}
	
	@Override
	public boolean contains(final Object o) {
		return(parent.contains(o));
	}
	@Override
	public int size() {
		return(parent.size());
	}
	@Override
	public boolean isEmpty() {
		return(parent.isEmpty());
	}
	
	@Override
	public void clear() {
		if(!isEmpty()) {
			parent.clear();
			fireChangeListener();
		}
	}
	
	@Override
	public void addChangeListener(final DataSink<? super MutableDequeModel<E>> l) {
		listeners.add(Requires.notNull(l, "l == null"));
	}
	@Override
	public void removeChangeListener(final DataSink<? super MutableDequeModel<E>> l) {
		listeners.remove(Requires.notNull(l, "l == null"));
	}
	
	private void fireChangeListener() {
		for(final DataSink<? super MutableDequeModel<E>> l:listeners) {
			l.sink(this);
		}
	}
}
