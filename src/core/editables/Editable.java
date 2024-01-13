package core.editables;

import java.util.Collection;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public interface Editable {
	
	Spatial getSpatial();
	
	boolean canHaveChilds();
	
	Editable getChild(int i);
	
	int getChildCount();
	
	void addChild(Editable child);
	
	void removeChild(Editable editable);
	
	void setSelected(boolean b);
	
	
	String getName();
	
	void setName(String newName);
	
	Vector3f getLocalTranslation();
	
	void setLocalTranslation(Vector3f vec);
	
	Vector3f getWorldTranslation();
	
	Quaternion getLocalRotation();
	
	void setLocalRotation(Quaternion quat);
	
	Vector3f getLocalScale();
	
	void setLocalScale(Vector3f vec);
	
	
	Collection<EditionMode> getEditionModes();
	
	Spatial createSpatial();
	
}
