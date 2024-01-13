package core.editables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class AudioNodeEditable implements Editable {
	private static final Collection<EditionMode> MODS = new ArrayList<>(1) {
		{
			add(EditionMode.NAME);
			add(EditionMode.LOCAL_TRANSLATION);
			add(EditionMode.LOCAL_ROTATION);
			add(EditionMode.LOCAL_SCALE);
			add(EditionMode.LOOPING);
		}
	};
	
	private final SimpleApplication app;
	private final Spatial spatial;
	
	private RigidBodyControl selectionControl;
	
	private final List<Editable> childs;
	
	private boolean looping;
	
	public AudioNodeEditable(final SimpleApplication app, final String name) {
		this.app = Requires.notNull(app, "app == null");
		
		spatial = app.getAssetManager().loadModel("Models/Speaker.obj");
		spatial.setName(Requires.notNull(name, "name == null"));
		childs = new ArrayList<>();
	}
	
	public AudioNodeEditable(final SimpleApplication app, final AudioNode node) {
		this.app = Requires.notNull(app, "app == null");
		
		spatial = app.getAssetManager().loadModel("Models/Speaker.obj");
		
		childs = new ArrayList<>();
		looping =  node.isLooping();
		spatial.setName(node.getName());
	}

	public void setLooping(final boolean looping) {
		this.looping = Requires.notNull(looping, "looping == null");
	}
	public boolean isLooping() {
		return (looping);
	}
	
	@Override
	public void addChild(final Editable child) {
		childs.add(child);
	}
	@Override
	public void removeChild(final Editable editable) {
		childs.remove(editable);
	}
	
	@Override
	public Spatial getSpatial() {
		return (spatial);
	}
	
	@Override
	public boolean canHaveChilds() {
		return(true);
	}
	@Override
	public Editable getChild(final int i) {
		return(childs.get(i));
	}
	@Override
	public int getChildCount() {
		return(childs.size());
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
		final AudioNode node = new AudioNode();
		node.setName(spatial.getName());
		node.setLocalTranslation(spatial.getLocalTranslation());
		node.setLocalScale(spatial.getLocalScale());
		node.setLocalRotation(spatial.getLocalRotation());
		// TODO audio settings
		for(int i = 0, size = childs.size(); i < size; i++) {
			node.attachChild(childs.get(i).createSpatial());
		}
		
		return(node);
	}
}
