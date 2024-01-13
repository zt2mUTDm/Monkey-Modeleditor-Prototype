package core.editables;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class GeometryEditable implements Editable {
	private static final Collection<EditionMode> MODS = new ArrayList<>(1) {
		{
			add(EditionMode.NAME);
			add(EditionMode.LOCAL_TRANSLATION);
			add(EditionMode.LOCAL_ROTATION);
			add(EditionMode.LOCAL_SCALE);
		}
	};
	
	private final SimpleApplication app;
	private final Geometry spatial;
	
	private RigidBodyControl selectionControl;
	
	public GeometryEditable(final SimpleApplication app, final Geometry geometry) {
		this.app = Requires.notNull(app, "app == null");
		this.spatial = Requires.notNull(geometry, "geometry == null");
	}
	
	@Override
	public void addChild(final Editable child) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean canHaveChilds() {
		return(false);
	}
	@Override
	public Editable getChild(final int i) {
		throw new ArrayIndexOutOfBoundsException();
	}
	@Override
	public int getChildCount() {
		return(0);
	}
	@Override
	public void removeChild(final Editable editable) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Spatial getSpatial() {
		return (spatial);
	}
	
	
	@Override
	public Vector3f getWorldTranslation() {
		return(spatial.getWorldTranslation());
	}
	
	@Override
	public void setSelected(final boolean b) {
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				final BulletAppState state = app.getStateManager().getState(BulletAppState.class);
				if(b) {
					if(selectionControl != null) {
						spatial.removeControl(selectionControl);
						state.getPhysicsSpace().remove(selectionControl);
					}
					
					final CollisionShape shape = CollisionShapeFactory.createMeshShape(spatial);
					selectionControl = new RigidBodyControl(shape, 1);
					selectionControl.setKinematic(true);
					spatial.addControl(selectionControl);
					state.getPhysicsSpace().add(selectionControl);
				} else {
					if(selectionControl != null) {
						spatial.removeControl(selectionControl);
						state.getPhysicsSpace().remove(selectionControl);
						
						selectionControl = null;
					}
				}
			}
		});
	}
	
	@Override
	public void setName(final String newName) {
		spatial.setName(newName);
	}
	@Override
	public String getName() {
		return(spatial.getName());
	}
	
	@Override
	public String toString() {
		return(getName());
	}
	
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(MODS);
	}
	
	@Override
	public Vector3f getLocalTranslation() {
		return(spatial.getLocalTranslation());
	}
	@Override
	public void setLocalTranslation(final Vector3f vec) {
		spatial.setLocalTranslation(vec);
	}
	
	@Override
	public Quaternion getLocalRotation() {
		return(spatial.getLocalRotation());
	}
	@Override
	public void setLocalRotation(final Quaternion quat) {
		this.spatial.setLocalRotation(quat);
	}
	
	@Override
	public Vector3f getLocalScale() {
		return(spatial.getLocalScale());
	}
	@Override
	public void setLocalScale(final Vector3f vec) {
		this.spatial.setLocalScale(vec);
		
		if(selectionControl != null) {
			vec.maxLocal(new Vector3f(0, 0, 0));
			
			selectionControl.setPhysicsScale(vec);
		}
	}
	
	@Override
	public Spatial createSpatial() {
		final Geometry geo = new Geometry();
		geo.setName(spatial.getName());
		geo.setLocalTranslation(spatial.getLocalTranslation());
		geo.setLocalScale(spatial.getLocalScale());
		geo.setLocalRotation(spatial.getLocalRotation());
		
		geo.setMesh(spatial.getMesh());
		geo.setMaterial(spatial.getMaterial());
		
		return(geo);
	}
}
