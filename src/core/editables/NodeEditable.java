package core.editables;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;

public class NodeEditable extends SpatialEditable {
	private final List<SpatialEditable> childs;
	
	protected NodeEditable(final Application app, final Node node) {
		super(app, node);
		this.childs = new ArrayList<>();
		
		for(final Spatial child:node.getChildren()) {
			childs.add(SpatialEditable.valueOf(app, child));
		}
	}
	
	@Override
	public Node getSpatial() {
		return (Node) (super.getSpatial());
	}
	
	public void addChild(final SpatialEditable child) {
		childs.add(child);
		getSpatial().attachChild(child.getSpatial());
	}
	public void addChild(final SpatialEditable child, final int index) {
		childs.add(index, child);
		getSpatial().attachChildAt(child.getSpatial(), index);
	}
	public int getChildIndex(final SpatialEditable child) {
		return(childs.indexOf(child));
	}
	public SpatialEditable getChild(final int i) {
		return(childs.get(i));
	}
	public int getChildCount() {
		return(childs.size());
	}
	public void removeChild(final SpatialEditable child) {
		childs.remove(child);
	}
	
	@Override
	public Node createSpatial() {
		final Node node = getSpatial();
		
		final Node newNode = new Node(node.getName());
		newNode.setLocalTranslation(node.getLocalTranslation());
		newNode.setLocalScale(node.getLocalScale());
		newNode.setLocalRotation(node.getLocalRotation());
		
		final Cloner cloner = new Cloner();
		for(final ControlEditable c:getControls()) {
			final Control oldControl = c.createControl();
			final Control newControl = cloner.clone(oldControl);
			newControl.setSpatial(null);
			newNode.addControl(newControl);
		}
		
		for(final SpatialEditable e:childs) {
			newNode.attachChild(e.createSpatial());
		}
		
		return(newNode);
	}
	
	public static NodeEditable valueOf(final Application app, final Node node) {
		return(new NodeEditable(app, node));
	}
}
