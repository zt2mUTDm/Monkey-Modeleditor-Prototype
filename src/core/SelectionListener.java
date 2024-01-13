package core;

public interface SelectionListener<T> {
	
	public void selectionAdded(Object source, T value);
	
	public void selectionRemoved(Object source, T value);
	
}
