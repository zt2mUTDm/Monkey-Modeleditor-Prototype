package core;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.StripBox;
import com.jme3.scene.shape.Torus;

import core.editables.AudioNodeEditable;
import core.editables.BoxEditable;
import core.editables.CylinderEditable;
import core.editables.DomeEditable;
import core.editables.Editable;
import core.editables.GeometryEditable;
import core.editables.NodeEditable;
import core.editables.SphereEditable;
import core.editables.TorusEditable;
import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;

public class SpatialToEditableConverter implements Converter<Spatial, Editable> {
	private final SimpleApplication app;
	
	public SpatialToEditableConverter(final SimpleApplication app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public Editable convert(final Spatial spatial) {
		Editable edit;
		if(spatial instanceof Node) {
			edit = parseNode((Node) spatial);
		} else if(spatial instanceof Geometry) {
			edit = parseGeometry((Geometry) spatial);
		} else {
			throw new IllegalStateException("Unknown spatial subtype");
		}
		
		edit.setName(spatial.getName());
		edit.setLocalTranslation(spatial.getLocalTranslation());
		edit.setLocalScale(spatial.getLocalScale());
		edit.setLocalRotation(spatial.getLocalRotation());
		
		return(edit);
	}
	private Editable parseGeometry(final Geometry geometry) {
		final Mesh mesh = geometry.getMesh();
		if(mesh instanceof Box || mesh instanceof StripBox) {
			return(new BoxEditable(app, geometry));
		} else if(mesh instanceof Cylinder) {
			return(new CylinderEditable(app, geometry));
		} else if(mesh instanceof Sphere) {
			return(new SphereEditable(app, geometry));
		} else if(mesh instanceof Dome) {
			return(new DomeEditable(app, geometry));
		} else if(mesh instanceof Torus) {
			return(new TorusEditable(app, geometry));
		} else {
			return(new GeometryEditable(app, geometry));
		}
	}
	private Editable parseNode(final Node node) {
		Editable edit;
		if(node instanceof AudioNode) {
			edit = new AudioNodeEditable(app, (AudioNode)node);
		} else {
			edit = new NodeEditable(app.getAssetManager());
		}
		
		for(final Spatial spatial:node.getChildren()) {
			edit.addChild(convert(spatial));
		}
		return(edit);
	}
}
