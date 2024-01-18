package core;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import core.editables.Editable;
import core.editables.NodeEditable;
import online.money_daisuki.api.base.Requires;

public final class ThreeDimensionalView {
	private final SimpleApplication app;
	
	private SelectionModel<Editable> selectionModel;
	private SelectionListener<Editable> selectionModelListener;
	
	private final Node content;
	
	public ThreeDimensionalView(final SimpleApplication app) {
		this.app = Requires.notNull(app, "app == null");
		
		content = new Node();
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				app.getRootNode().attachChild(content);
			}
		});
	}
	
	public void setSelectionModel(final SelectionModel<Editable> newSelectionModel) {
		if(selectionModel != null) {
			selectionModel.removeSelectionChangedListener(selectionModelListener);
		}
		
		if(newSelectionModel != null) {
			selectionModel = newSelectionModel;
			
			selectionModelListener = new SelectionListener<Editable>() {
				@Override
				public void selectionAdded(final Object source, final Editable value) {
					
				}
				@Override
				public void selectionRemoved(final Object source, final Editable value) {
					
				}
			};
			selectionModel.addSelectionChangedListener(selectionModelListener);
		} else {
			selectionModel = null;
			selectionModelListener = null;
		}
	}
	
	public SimpleApplication getApplication() {
		return(app);
	}
	
	public void setObject(final Editable object) {
		addObject(object, content);
	}
	private void addObject(final Editable object, final Node parent) {
		final Spatial spatial = object.getSpatial();
		parent.attachChild(spatial);
		
		if(object instanceof NodeEditable) {
			final NodeEditable asNode = (core.editables.NodeEditable) object;
			for(int i = 0, size = asNode.getChildCount(); i < size; i++) {
				addObject(asNode.getChild(i), (Node) spatial);
			}
		}
	}
	public void addObject(final Editable object, final NodeEditable parent) {
		addObject(object, (Node)parent.getSpatial());
	}

	public void clear() {
		selectionModel.clear();
		
		Utils.enqueueAndWait(app, new Runnable() {
			@Override
			public void run() {
				content.detachAllChildren();
			}
		});
	}
}
