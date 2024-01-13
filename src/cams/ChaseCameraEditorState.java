package cams;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public final class ChaseCameraEditorState extends BaseAppState implements EditorCamera {
	private boolean init;
	
	private Node node;
	private ChaseCamera cam;
	
	@Override
	protected void initialize(final Application app) {
		if(!init) {
			app.getCamera().setRotation(new Quaternion(0, 1, 0, 0));
			
			node = new Node();
			
			final Box b = new Box(0.2f, 0.2f, 0.2f);
			
			final Geometry geo = new Geometry("ChaseCamera node geometry", b);
			
			final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", ColorRGBA.Blue);
			geo.setMaterial(mat);
			
			node.attachChild(geo);
			
			createCam();
			
			init = true;
		}
	}
	private void createCam() {
		final Application app = getApplication();
		final InputManager input = app.getInputManager();
		
		cam = new ChaseCamera(app.getCamera(), input);
		input.deleteTrigger(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		//input.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		cam.setEnabled(false);
		cam.setMaxDistance(Float.POSITIVE_INFINITY);
		cam.setMinVerticalRotation((float)(-cam.getMaxVerticalRotation() - 0.1));
		node.addControl(cam);
	}
	
	@Override
	protected void onEnable() {
		getApplication().getCamera().setRotation(new Quaternion(0, 0, 1, 0));
		((SimpleApplication)getApplication()).getRootNode().attachChild(node);
		cam.setEnabled(true);
	}
	@Override
	protected void onDisable() {
		cam.setEnabled(false);
		((SimpleApplication)getApplication()).getRootNode().detachChild(node);
	}
	
	@Override
	protected void cleanup(final Application app) {
		
	}
	
	@Override
	public void move(final Vector3f vec) {
		node.setLocalTranslation(vec);
	}
	
	@Override
	public void reset() {
		cam.setEnabled(false);
		node.removeControl(cam);
		cam.cleanupWithInput(getApplication().getInputManager());
		
		getApplication().getCamera().setRotation(new Quaternion(0, 1, 0, 0));
		
		createCam();
		cam.setEnabled(true);
	}
}
