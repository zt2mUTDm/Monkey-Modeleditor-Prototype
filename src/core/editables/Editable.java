package core.editables;

import java.util.Collection;

import com.jme3.scene.Spatial;

public interface Editable {
	
	Spatial getSpatial();
	
	void setSelected(boolean b);
	
	Collection<EditionMode> getEditionModes();
	
}
