package online.money_daisuki.api.base.models;

import online.money_daisuki.api.base.DataSink;

public interface MutableDequeModel<E> extends DequeModel<E> {
	
	void addFirst(E e);
	
	void addLast(E e);
	
	E removeFirst();
	
	E removeLast();
	
	void addChangeListener(DataSink<? super MutableDequeModel<E>> l);
	
	void removeChangeListener(DataSink<? super MutableDequeModel<E>> l);
	
	void clear();
	
}
