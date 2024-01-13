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
import com.jme3.scene.shape.Torus;

import online.money_daisuki.api.base.Requires;

public final class TorusEditable implements Editable, HasColor, HasCircleSamples, HasRadialSamples, HasInnerRadius,
		HasOuterRadius {
	private static final Collection<EditionMode> MODS = new ArrayList<>(1) {
		{
			add(EditionMode.NAME);
			add(EditionMode.LOCAL_TRANSLATION);
			add(EditionMode.LOCAL_ROTATION);
			add(EditionMode.LOCAL_SCALE);
			
			add(EditionMode.CIRCLE_SAMPLES);
			add(EditionMode.RADIAL_SAMPLES);
			add(EditionMode.INNER_RADIUS);
			add(EditionMode.OUTER_RADIUS);
			
			add(EditionMode.COLOR);
		}
	};
	
	private final SimpleApplication app;
	private final Geometry spatial;
	
	private RigidBodyControl selectionControl;
	
	private int circleSamples = 16;
	private int radialSamples = 32;
	private float innerRadius = 0.33f;
	private float outerRadius = 1.0f;
	
	private ColorRGBA color;
	
	public TorusEditable(final SimpleApplication app, final String name) {
		this.app = Requires.notNull(app, "app == null");
		
		this.color = ColorRGBA.randomColor();
		
		final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		
		spatial = new Geometry(Requires.notNull(name, "name == null"), createMesh());
		spatial.setMaterial(mat);
	}
	public TorusEditable(final SimpleApplication app, final Geometry geometry) {
		this.app = Requires.notNull(app, "app == null");
		
		final Torus mesh = (Torus) geometry.getMesh();
		
		circleSamples = mesh.getCircleSamples();
		radialSamples = mesh.getRadialSamples();
		innerRadius = mesh.getInnerRadius();
		outerRadius = mesh.getOuterRadius();
		
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
		return(new Torus(circleSamples, radialSamples, innerRadius, outerRadius));
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
	public int getCircleSamples() {
		return (circleSamples);
	}
	@Override
	public void setCircleSamples(final int circleSamples) {
		if(circleSamples != this.circleSamples) {
			this.circleSamples = circleSamples;
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
	public float getInnerRadius() {
		return (innerRadius);
	}
	@Override
	public void setInnerRadius(final float innerRadius) {
		if(innerRadius != this.innerRadius) {
			this.innerRadius = innerRadius;
			updateMesh();
		}
	}
	
	@Override
	public float getOuterRadius() {
		return (outerRadius);
	}
	@Override
	public void setOuterRadius(final float outerRadius) {
		if(outerRadius != this.outerRadius) {
			this.outerRadius = outerRadius;
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
