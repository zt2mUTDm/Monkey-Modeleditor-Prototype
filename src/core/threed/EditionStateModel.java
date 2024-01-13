package core.threed;

import com.jme3.math.Vector3f;

import core.GridMode;

public interface EditionStateModel {
	
	void set(String vecToStr);
	
	void setTemporary(String vecToStr);
	
	String get();
	
	Vector3f getCursorLocation();
	
	float getGridSize();
	
	GridMode getGridMode();
	
}
