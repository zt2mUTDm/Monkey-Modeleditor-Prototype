package core.editables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public abstract class SpatialEditable implements Editable {
	private final Application app;
	private final Spatial spatial;
	private final List<EditionMode> modes;
	
	public SpatialEditable(final Application app, final Spatial spatial) {
		this.app = Requires.notNull(app, "app == null");
		this.spatial = Requires.notNull(spatial, "spatial == null");
		
		this.modes = new ArrayList<>();
		addMode(new NameEditionMode(this));
		addMode(new LocalTranslationEditionMode(this));
		addMode(new LocalRotationEditionMode(this));
		addMode(new LocalScaleEditionMode(this));
	}
	protected void addMode(final EditionMode mode) {
		this.modes.add(Requires.notNull(mode, "mode == null"));
	}
	
	@Override
	public Spatial getSpatial() {
		return(spatial);
	}
	
	@Override
	public void setSelected(final boolean b) {
		
	}
	
	public String getName() {
		return(spatial.getName());
	}
	
	public void setName(final String newName) {
		spatial.setName(Requires.notNull(newName));
	}
	
	public Vector3f getLocalTranslation() {
		return(new Vector3f(spatial.getLocalTranslation()));
	}
	
	public void setLocalTranslation(final Vector3f vec) {
		spatial.setLocalTranslation(vec);
	}
	
	public Vector3f getWorldTranslation() {
		return(new Vector3f(spatial.getWorldTranslation()));
	}
	
	public Quaternion getLocalRotation() {
		return(new Quaternion(spatial.getLocalRotation()));
	}
	
	public void setLocalRotation(final Quaternion quat) {
		spatial.setLocalRotation(quat);
	}
	
	public Vector3f getLocalScale() {
		return(spatial.getLocalScale());
	}
	
	public void setLocalScale(final Vector3f vec) {
		spatial.setLocalScale(new Vector3f(vec));
	}
	
	@Override
	public String toString() {
		return(spatial.getName());
	}
	
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(Collections.unmodifiableCollection(modes));
	}
	
	public abstract Spatial createSpatial();
	
	protected Application getApplication() {
		return (app);
	}
	
	public static SpatialEditable valueOf(final Application app, final Spatial spatial) {
		SpatialEditable edit;
		if(spatial instanceof Geometry) {
			edit = GeometryEditable.valueOf(app, (Geometry)spatial);
		} else if(spatial instanceof Node) {
			edit = NodeEditable.valueOf(app, (Node)spatial);
		} else {
			throw new IllegalAccessError("Unknown spatial type");
		}
		
		edit.setName(spatial.getName() != null ? spatial.getName() : ""); //TODO
		edit.setLocalTranslation(spatial.getLocalTranslation());
		edit.setLocalRotation(spatial.getLocalRotation());
		edit.setLocalScale(spatial.getLocalScale());
		
		return(edit);
	}
}
