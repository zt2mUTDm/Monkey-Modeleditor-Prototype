package core.editables;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class NodeEditable extends SpatialEditable {
	private final List<SpatialEditable> childs;
	
	public NodeEditable(final Application app, final Node node) {
		super(app, node);
		this.childs = new ArrayList<>();
	}
	
	public void addChild(final SpatialEditable child) {
		childs.add(child);
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
	public Spatial createSpatial() {
		final Node node = (Node) getSpatial();
		
		final Node newNode = new Node(node.getName());
		newNode.setLocalTranslation(node.getLocalTranslation());
		newNode.setLocalScale(node.getLocalScale());
		newNode.setLocalRotation(node.getLocalRotation());
		
		for(final SpatialEditable e:childs) {
			newNode.attachChild(e.createSpatial());
		}
		
		return(newNode);
	}
	
	public static NodeEditable valueOf(final Application app, final Node node) {
		final NodeEditable nodeEditable = new NodeEditable(app, new Node());
		
		for(final Spatial child:node.getChildren()) {
			nodeEditable.addChild(SpatialEditable.valueOf(app, child));
		}
		
		return(nodeEditable);
	}
}
