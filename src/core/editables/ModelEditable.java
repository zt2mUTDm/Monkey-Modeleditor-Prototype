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
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class ModelEditable implements Editable {
	private static final Collection<EditionMode> MODS = new ArrayList<>(1) {
		{
			add(EditionMode.NAME);
			add(EditionMode.LOCAL_TRANSLATION);
			add(EditionMode.LOCAL_ROTATION);
			add(EditionMode.LOCAL_SCALE);
		}
	};
	
	private final SimpleApplication app;
	private final Spatial spatial;
	
	private RigidBodyControl selectionControl;
	
	public ModelEditable(final SimpleApplication app, final String url) {
		this(app, url, "Model");
	}
	public ModelEditable(final SimpleApplication app, final String url, final String name) {
		this.app = Requires.notNull(app, "app == null");
		spatial = app.getAssetManager().loadModel(Requires.notNull(url, "url == null"));
		spatial.setName(Requires.notNull(name, "name == null"));
	}
	
	@Override
	public void addChild(final Editable child) {
		throw new UnsupportedOperationException();
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
		final Vector3f newVec = new Vector3f(vec);
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				spatial.setLocalTranslation(newVec);
			}
		});
	}
	
	@Override
	public Quaternion getLocalRotation() {
		return(spatial.getLocalRotation());
	}
	@Override
	public void setLocalRotation(final Quaternion quat) {
		final Quaternion newQuat = new Quaternion(quat);
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				spatial.setLocalRotation(newQuat);
			}
		});
	}
	
	@Override
	public Vector3f getLocalScale() {
		return(spatial.getLocalScale());
	}
	@Override
	public void setLocalScale(final Vector3f vec) {
		final Vector3f newVec = new Vector3f(vec);
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				spatial.setLocalScale(newVec);
				
				if(selectionControl != null) {
					vec.maxLocal(new Vector3f(0, 0, 0));
					
					selectionControl.setPhysicsScale(newVec);
				}
			}
		});
	}
	
	@Override
	public void setName(final String newName) {
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				spatial.setName(newName);
			}
		});
	}
	@Override
	public String getName() {
		return(spatial.getName());
	}
	
	@Override
	public Spatial createSpatial() {
		throw new UnsupportedOperationException();
	}
}
