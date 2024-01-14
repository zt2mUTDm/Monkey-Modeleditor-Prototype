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
import com.jme3.scene.shape.AbstractBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.StripBox;

import online.money_daisuki.api.base.Requires;

public final class BoxEditable implements Editable, HasColor {
	private static final Collection<EditionMode> MODS = new ArrayList<>(1) {
		{
			add(EditionMode.NAME);
			add(EditionMode.LOCAL_TRANSLATION);
			add(EditionMode.LOCAL_ROTATION);
			add(EditionMode.LOCAL_SCALE);
			add(EditionMode.EXTENDS);
			add(EditionMode.COLOR);
			add(EditionMode.STRIP);
		}
	};
	
	private final SimpleApplication app;
	private final Geometry spatial;
	
	private RigidBodyControl selectionControl;
	
	private Vector3f ext;
	
	private ColorRGBA color;
	
	private boolean strip;
	
	public BoxEditable(final SimpleApplication app, final Vector3f ext, final String name) {
		this.app = Requires.notNull(app, "app == null");
		
		this.ext = new Vector3f(ext);
		
		this.color = ColorRGBA.randomColor();
		
		final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		
		spatial = new Geometry(name, createMesh());
		spatial.setMaterial(mat);
	}	
	public BoxEditable(final SimpleApplication app, final Geometry geometry) {
		this.app = Requires.notNull(app, "app == null");
		
		final Mesh mesh = (AbstractBox)geometry.getMesh();
		if(mesh instanceof StripBox) {
			strip = true;
		} else if(!(mesh instanceof AbstractBox)) {
			throw new IllegalArgumentException();
		}
		
		final AbstractBox boxMesh = (AbstractBox) mesh;
		this.ext = new Vector3f(boxMesh.getXExtent(), boxMesh.getYExtent(), boxMesh.getZExtent());
		
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
		return(strip ? new StripBox(ext.x, ext.y, ext.z) : new Box(ext.x, ext.y, ext.z));
	}
	
	public boolean isStrip() {
		return (strip);
	}
	public void setStrip(final boolean strip) {
		if(this.strip != strip) {
			this.strip = strip;
			app.enqueue(new Runnable() {
				@Override
				public void run() {
					spatial.setMesh(createMesh());
				}
			});
		}
	}
	
	public Vector3f getExtends() {
		return(new Vector3f(ext));
	}
	public void setExtends(final Vector3f vec) {
		this.ext = new Vector3f(vec);
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				spatial.setMesh(createMesh());
			}
		});
	}
	
	@Override
	public ColorRGBA getColor() {
		return(color);
	}
	@Override
	public void setColor(final ColorRGBA color) {
		this.color = new ColorRGBA(color);
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				spatial.getMaterial().setColor("Color", color);
			}
		});
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
