package core.editables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;

import online.money_daisuki.api.base.Requires;

@SuppressWarnings("deprecation")
public abstract class SpatialEditable implements Editable {
	private final Application app;
	private final Spatial spatial;
	private final List<EditionMode> modes;
	private final List<ControlEditable> controls;
	
	public SpatialEditable(final Application app, final Spatial spatial) {
		this.app = Requires.notNull(app, "app == null");
		this.spatial = Requires.notNull(spatial, "spatial == null");
		
		this.modes = new ArrayList<>();
		addMode(new NameEditionMode(this));
		addMode(new LocalTranslationEditionMode(this));
		addMode(new LocalRotationEditionMode(this));
		addMode(new LocalScaleEditionMode(this));
		
		this.controls = new LinkedList<>();
	}
	protected void addMode(final EditionMode mode) {
		this.modes.add(Requires.notNull(mode, "mode == null"));
	}
	
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
	
	public void addControl(final ControlEditable edit) {
		controls.add(Requires.notNull(edit, "edit == null"));
		if(edit instanceof PhysicsControlEditable) {
			final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
			final Control control = edit.getControl();
			
			spatial.addControl(control);
			bullet.getPhysicsSpace().add(control);
		}
	}
	public void addControl(final ControlEditable edit, final int index) {
		controls.add(index, Requires.notNull(edit, "edit == null"));
		if(edit instanceof PhysicsControlEditable) {
			final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
			final Control control = edit.getControl();
			
			spatial.addControl(control);
			bullet.getPhysicsSpace().add(control);
		}
	}
	public int getControlIndex(final ControlEditable ce) {
		return(controls.indexOf(Requires.notNull(ce, "ce == null")));
	}
	public void removeControl(final ControlEditable edit) {
		final boolean b = controls.remove(Requires.notNull(edit, "edit == null"));
		if(b && edit instanceof PhysicsControlEditable) {
			final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
			final Control control = edit.getControl();
			
			spatial.removeControl(control);
			bullet.getPhysicsSpace().remove(control);
		}
	}
	public List<ControlEditable> getControls() {
		return (new ArrayList<>(controls));
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
		
		edit.setName(spatial.getName() != null ? spatial.getName() : "");
		edit.setLocalTranslation(spatial.getLocalTranslation());
		edit.setLocalRotation(spatial.getLocalRotation());
		edit.setLocalScale(spatial.getLocalScale());
		
		final Cloner cloner = new Cloner();
		for(int i = 0, size = spatial.getNumControls(); i < size; i++) {
			final Control control = spatial.getControl(i);
			
			if(control instanceof AnimControl) {
				edit.addControl(AnimControlEditable.valueOf((AnimControl)control));
			} else if(control instanceof AnimComposer) {
				final AnimComposer copy = cloner.clone((AnimComposer)control);
				copy.setSpatial(null);
				
				edit.addControl(AnimComposerEditable.valueOf(copy));
				
				if(spatial.getControl(AnimationControl.class) == null) {
					edit.addControl(AnimationControlEditable.valueOf(new AnimationControl()));
				}
			} else if(control instanceof PhysicsControl) {
				edit.addControl(PhysicsControlEditable.valueOf((PhysicsControl)control));
			} else if(control instanceof SkinningControl) {
				edit.addControl(SkinningControlEditable.valueOf((SkinningControl)control));
			} else {
				edit.addControl(ControlEditable.valueOf(control));
			}
		}
		return(edit);
	}
}
