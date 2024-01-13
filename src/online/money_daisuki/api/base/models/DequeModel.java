package online.money_daisuki.api.base.models;

public interface DequeModel<E> {
	
	E getFirst();
	
	E getLast();
	
	boolean contains(Object o);
	
	int size();
	
	boolean isEmpty();
	
}
