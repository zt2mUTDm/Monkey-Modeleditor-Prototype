package core.editables;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class GeometryEditable extends SpatialEditable {
	private RigidBodyControl selectionControl;
	private MeshEditable mesh;
	private DataSink<? super MeshEditable> meshChangedListener;
	
	public GeometryEditable(final Application app, final Geometry geo, final MeshEditable mesh) {
		super(app, geo);
		
		setMesh(Requires.notNull(mesh, "mesh == null"));
		setName(geo.getName());
	}
	
	private void setMesh(final MeshEditable mesh) {
		Requires.notNull(mesh, "mesh == null");
		
		if(this.mesh != null) {
			this.mesh.removeMeshChangedListener(meshChangedListener);
		}
		
		meshChangedListener = new DataSink<MeshEditable>() {
			@Override
			public void sink(final MeshEditable value) {
				((Geometry)getSpatial()).setMesh(value.getMesh());
			}
		};
		mesh.addMeshChangedListener(meshChangedListener);
		this.mesh = mesh;
	}

	@Override
	public void setSelected(final boolean b) {
		final Application app = getApplication();
		final Spatial spatial = getSpatial();
		
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
	
	public MeshEditable getMesh() {
		return(mesh);
	}
	
	@Override
	public Spatial createSpatial() {
		final Geometry geo = (Geometry) getSpatial();
		
		final Geometry newGeo = new Geometry(geo.getName(), mesh.createMesh());
		newGeo.setLocalTranslation(geo.getLocalTranslation());
		newGeo.setLocalScale(geo.getLocalScale());
		newGeo.setLocalRotation(geo.getLocalRotation());
		newGeo.setMaterial(geo.getMaterial().clone());
		
		for(final ControlEditable c:getControls()) {
			newGeo.addControl(c.createControl());
		}
		
		return(newGeo);
	}
	
	static GeometryEditable valueOf(final Application app, final Geometry geo) {
		return(new GeometryEditable(app, geo, MeshEditable.valueOf(geo.getMesh())));
	}
}
