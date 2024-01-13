package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import online.money_daisuki.api.base.Requires;

public final class SelectionModelImpl<T> implements SelectionModel<T> {
	private final Set<T> selection;
	private final List<SelectionListener<? super T>> selectionChangedListeners;
	
	public SelectionModelImpl() {
		selection = new HashSet<>();
		selectionChangedListeners = new LinkedList<>();
	}
	
	@Override
	public void add(final T value) {
		if(selection.add(Requires.notNull(value, "value == null"))) {
			fireSelectionAddedListeners(value);
		}
	}
	@Override
	public void remove(final T value) {
		if(selection.remove(Requires.notNull(value, "value == null"))) {
			fireSelectionRemovedListeners(value);
		}
	}
	@Override
	public boolean contains(final T value) {
		return(selection.contains(Requires.notNull(value, "value == null")));
	}
	@Override
	public void clear() {
		for(final T value:selection) {
			fireSelectionRemovedListeners(value);
		}
		selection.clear();
	}
	
	@Override
	public void addSelectionChangedListener(final SelectionListener<? super T> l) {
		selectionChangedListeners.add(Requires.notNull(l, "l == null"));
	}
	@Override
	public Collection<SelectionListener<? super T>> getSelectionChangedListeners() {
		return(new ArrayList<>(selectionChangedListeners));
	}
	@Override
	public boolean removeSelectionChangedListener(final SelectionListener<? super T> l) {
		return(selectionChangedListeners.remove(Requires.notNull(l, "l == null")));
	}
	@Override
	public void clearSelectionChangedListeners() {
		selectionChangedListeners.clear();
	}
	
	private void fireSelectionAddedListeners(final T value) {
		for(final SelectionListener<?super T> l:selectionChangedListeners) {
			l.selectionAdded(this, value);
		}
	}
	private void fireSelectionRemovedListeners(final T value) {
		for(final SelectionListener<?super T> l:selectionChangedListeners) {
			l.selectionRemoved(this, value);
		}
	}
	
	@Override
	public Collection<T> getSelection() {
		return(new ArrayList<>(selection));
	}
}
