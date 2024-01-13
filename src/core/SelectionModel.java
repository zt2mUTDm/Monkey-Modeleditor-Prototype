package core;

import java.util.Collection;

public interface SelectionModel<T> {
	
	void add(T value);
	
	void remove(T value);
	
	boolean contains(T value);
	
	void clear();
	
	
	void addSelectionChangedListener(SelectionListener<? super T> l);
	
	boolean removeSelectionChangedListener(SelectionListener<? super T> l);
	
	Collection<SelectionListener<? super T>> getSelectionChangedListeners();
	
	void clearSelectionChangedListeners();
	
	
	Collection<T> getSelection();
	
}
