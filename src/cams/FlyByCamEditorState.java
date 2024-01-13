package cams;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public final class FlyByCamEditorState extends BaseAppState implements EditorCamera {
	private Vector3f vec;
	private Quaternion quat;
	
	@Override
	protected void initialize(final Application app) {
		
	}
	@Override
	protected void onEnable() {
		if(vec != null) {
			getApplication().getCamera().setLocation(vec);
			vec = null;
		}
		if(quat != null) {
			getApplication().getCamera().setRotation(quat);
			quat = null;
		}
		
		((SimpleApplication)getApplication()).getFlyByCamera().setEnabled(true);
	}
	@Override
	protected void onDisable() {
		vec = new Vector3f(getApplication().getCamera().getLocation());
		quat = new Quaternion(getApplication().getCamera().getRotation());
		((SimpleApplication)getApplication()).getFlyByCamera().setEnabled(false);
	}
	@Override
	protected void cleanup(final Application app) {
		
	}
	
	@Override
	public void move(final Vector3f vec) {
		getApplication().getCamera().setLocation(new Vector3f(vec));
	}
	@Override
	public void reset() {
		final Camera cam = getApplication().getCamera();
		cam.setLocation(new Vector3f(0.0f, 0.0f, 10.0f));
		cam.setRotation(new Quaternion(0.0f, 1.0f, 0.0f, 0.0f));
	}
}
