package core.editables;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import online.money_daisuki.api.base.Requires;

public final class SphereEditable implements Editable, HasColor, HasZSamples, HasRadialSamples, HasRadius,
		HasUseEvenSlices, HasInterior {
	private static final Collection<EditionMode> MODS = new ArrayList<>(1) {
		{
			add(EditionMode.NAME);
			add(EditionMode.LOCAL_TRANSLATION);
			add(EditionMode.LOCAL_ROTATION);
			add(EditionMode.LOCAL_SCALE);
			
			add(EditionMode.Z_SAMPLES);
			add(EditionMode.RADIAL_SAMPLES);
			add(EditionMode.RADIUS);
			//add(EditionMode.USE_EVEN_SLICES);
			//add(EditionMode.INTERIOR);
			
			add(EditionMode.COLOR);
		}
	};
	
	private final SimpleApplication app;
	private final Geometry spatial;
	
	private RigidBodyControl selectionControl;
	
	private int zSamples = 16;
	private int radialSamples = 32;
	private float radius = 1f;
	private boolean useEvenSlices;
	private boolean interior;
	
	private ColorRGBA color;
	
	public SphereEditable(final SimpleApplication app, final String name) {
		this.app = Requires.notNull(app, "app == null");
		
		this.color = ColorRGBA.randomColor();
		
		final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		
		spatial = new Geometry("<TranslateGeo>", createMesh());
		spatial.setName(Requires.notNull(name, "name == null"));
		spatial.setMaterial(mat);
	}
	public SphereEditable(final SimpleApplication app, final Geometry geometry) {
		this.app = Requires.notNull(app, "app == null");
		
		final Sphere mesh = (Sphere) geometry.getMesh();
		
		zSamples = mesh.getZSamples();
		radialSamples = mesh.getRadialSamples();
		radius = mesh.getRadius();
		
		final Material mat = geometry.getMaterial();
		final MatParam param = mat.getParam("Color");
		
		if(param != null) {
			color = (ColorRGBA) param.getValue();
		} else {
			color = ColorRGBA.randomColor(); //TODO
		}
		
		spatial = new Geometry(geometry.getName(), createMesh());
		spatial.setMaterial(geometry.getMaterial());
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
	
	private Mesh createMesh() {
		return(new Sphere(zSamples, radialSamples, radius, useEvenSlices, interior));
	}
	
	private void updateMesh() {
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				spatial.setMesh(createMesh());
				
				if(selectionControl != null) {
					setSelected(false);
					setSelected(true);
				}
			}
		});
	}
	
	
	@Override
	public int getZSamples() {
		return (zSamples);
	}
	@Override
	public void setZSamples(final int zSamples) {
		if(zSamples != this.zSamples) {
			this.zSamples = zSamples;
			updateMesh();
		}
	}
	
	@Override
	public int getRadialSamples() {
		return (radialSamples);
	}
	@Override
	public void setRadialSamples(final int radialSamples) {
		if(radialSamples != this.radialSamples) {
			this.radialSamples = radialSamples;
			updateMesh();
		}
	}
	
	@Override
	public float getRadius() {
		return (radius);
	}
	@Override
	public void setRadius(final float radius) {
		if(radius != this.radius) {
			this.radius = radius;
			updateMesh();
		}
	}
	
	@Override
	public boolean isUseEvenSlices() {
		return (useEvenSlices);
	}
	@Override
	public void setUseEvenSlices(final boolean useEvenSlices) {
		if(useEvenSlices != this.useEvenSlices) {
			this.useEvenSlices = useEvenSlices;
			updateMesh();
		}
	}
	
	@Override
	public boolean isInterior() {
		return (interior);
	}
	@Override
	public void setInterior(final boolean interior) {
		if(interior != this.interior) {
			this.interior = interior;
			updateMesh();
		}
	}
	
	@Override
	public ColorRGBA getColor() {
		return(color);
	}
	@Override
	public void setColor(final ColorRGBA color) {
		this.color = new ColorRGBA(color);
		this.spatial.getMaterial().setColor("Color", this.color);
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
