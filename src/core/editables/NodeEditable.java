package core.editables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import online.money_daisuki.api.base.Requires;

public final class NodeEditable implements Editable {
	private static final Collection<EditionMode> MODS = new ArrayList<>(1) {
		{
			add(EditionMode.NAME);
			add(EditionMode.LOCAL_TRANSLATION);
			add(EditionMode.LOCAL_ROTATION);
			add(EditionMode.LOCAL_SCALE);
		}
	};
	
	private final SimpleApplication app;
	
	private final Node spatial;
	private final Geometry geo;
	private final Material selectedMat;
	private final Material unselectedMat;
	
	private final List<Editable> childs;
	
	public NodeEditable(final SimpleApplication app) {
		this(app, "Node");
	}
	public NodeEditable(final SimpleApplication app, final String name) {
		this(app, name, Vector3f.ZERO);
	}
	public NodeEditable(final SimpleApplication app, final String name, final Vector3f translation) {
		this.app = Requires.notNull(app, "app == null");
		
		childs = new ArrayList<>();
		
		spatial = new Node();
		spatial.setName(name);
		spatial.setLocalTranslation(new Vector3f(translation));
		
		final Box b = new Box(0.1f, 0.1f, 0.1f);
		
		geo = new Geometry("NodeGeometry", b);
		
		selectedMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		selectedMat.setColor("Color", ColorRGBA.Red);
		
		unselectedMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		unselectedMat.setColor("Color", ColorRGBA.White);
		
		geo.setMaterial(unselectedMat);
		spatial.attachChild(geo);
	}
	
	@Override
	public void addChild(final Editable child) {
		childs.add(child);
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
	public void removeChild(final Editable editable) {
		childs.remove(editable);
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
				geo.setMaterial(b ? selectedMat : unselectedMat);
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
		final Node node = new Node();
		node.setName(spatial.getName());
		node.setLocalTranslation(spatial.getLocalTranslation());
		node.setLocalScale(spatial.getLocalScale());
		node.setLocalRotation(spatial.getLocalRotation());
		
		for(int i = 0, size = childs.size(); i < size; i++) {
			node.attachChild(childs.get(i).createSpatial());
		}
		
		return(node);
	}
}
