package cams;

import com.jme3.app.state.AppState;
import com.jme3.math.Vector3f;

public interface EditorCamera extends AppState {
	
	void reset();
	
	void move(Vector3f vec);
	
}
