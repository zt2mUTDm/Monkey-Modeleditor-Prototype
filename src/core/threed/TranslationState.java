package core.threed;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.TempVars;

import cams.EditorCamera;
import core.editables.EditionMode;
import core.ui.swing.GridMode;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.ValueChangedHandler;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModel;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class TranslationState extends BaseAppState implements EditionState {
	//private final EditionStateModel model;
	
	private Node node;
	
	private Vector3f clickedLocation;
	private Vector3f clickedNodeLocation;
	private Vector3f clickedCollisionsPointNodeDifference;
	private float clickedDistanceToTarget;
	private Vector3f clickedValue;
	
	private final CollisionResults colResults = new CollisionResults();
	
	private final SetableMutableSingleValueModel<Wrappler> selectedElement;
	
	private final Collection<Wrappler> wrapplers;
	
	private Camera cursorViewportCamera;
	private ViewPort cursorViewport;
	private Node cursorViewportNode;
	
	private final EditionStateModel model;
	private final EditionMode mode;
	private final EditionMode worldCoordinates;
	
	private Geometry debugGeo0;
	private Geometry debugGeo1;
	private Geometry debugGeo2;
	
	public TranslationState(final EditionStateModel model, final EditionMode mode, final EditionMode worldCoordinates) {
		this.model = Requires.notNull(model, "model == null");
		this.mode = Requires.notNull(mode, "mode == null");
		this.worldCoordinates = Requires.notNull(worldCoordinates, "worldCoordinates == null");
		this.selectedElement = new SetableMutableSingleValueModelImpl<>();
		this.wrapplers = new ArrayList<>(6);
	}
	@Override
	protected void initialize(final Application app) {
		cursorViewportCamera = app.getCamera().clone();
		cursorViewportCamera.setViewPort(0.0f, 1.0f, 0.0f, 1.0f);
		cursorViewportCamera.setLocation(app.getCamera().getLocation());
		cursorViewportCamera.setRotation(app.getCamera().getRotation());
		
		cursorViewportNode = new Node();
		
		createDebug();
		
		cursorViewport = app.getRenderManager().createMainView("Vector3f cursor", cursorViewportCamera);
		cursorViewport.setClearFlags(false, true, false);
		cursorViewport.attachScene(cursorViewportNode);
		
		
		final Vector3f initLocation = (Vector3f) worldCoordinates.get();
		
		node = new Node();
		node.setLocalTranslation(initLocation.add(0, 0, 0));
		
		
		final Material redArrowMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		redArrowMat.setColor("Color", ColorRGBA.Red);
		
		final Spatial redArrowGeo = ((Node)((Node)((Node)app.getAssetManager().loadModel("Models/indicators/arrow/arrow.j3o")).getChild(0)).getChild(0)).getChild(0);
		redArrowGeo.setName("<TranslateGeo>");
		redArrowGeo.setLocalTranslation(-1.0f, -1.0f, -1.0f);
		redArrowGeo.setLocalScale(2, 2, 2);
		redArrowGeo.setMaterial(redArrowMat);
		node.attachChild(redArrowGeo);
		wrapplers.add(new Wrappler(redArrowGeo, new Vector3f(1, 0, 0)));
		
		
		final Material greenArrowMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		greenArrowMat.setColor("Color", ColorRGBA.Green);
		
		final Spatial greenArrowGeo = ((Node)((Node)((Node)app.getAssetManager().loadModel("Models/indicators/arrow/arrow.j3o")).getChild(0)).getChild(0)).getChild(0);
		greenArrowGeo.setName("<TranslateGeo>");
		greenArrowGeo.setLocalTranslation(-1.0f, -1.0f, -1.0f);
		greenArrowGeo.rotate(0, 0, (float) (Math.PI / 2));
		greenArrowGeo.setLocalScale(2, 2, 2);
		greenArrowGeo.setMaterial(greenArrowMat);
		node.attachChild(greenArrowGeo);
		wrapplers.add(new Wrappler(greenArrowGeo, new Vector3f(0, 1, 0)));
		
		
		final Material blueArrowMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		blueArrowMat.setColor("Color", ColorRGBA.Blue);
		
		final Spatial blueArrowGeo = ((Node)((Node)((Node)app.getAssetManager().loadModel("Models/indicators/arrow/arrow.j3o")).getChild(0)).getChild(0)).getChild(0);
		blueArrowGeo.setName("<TranslateGeo>");
		blueArrowGeo.setLocalTranslation(-1.0f, -1.0f, -1.0f);
		blueArrowGeo.rotate(0, (float) -(Math.PI / 2), 0);
		blueArrowGeo.setLocalScale(2, 2, 2);
		blueArrowGeo.setMaterial(blueArrowMat);
		node.attachChild(blueArrowGeo);
		wrapplers.add(new Wrappler(blueArrowGeo, new Vector3f(0, 0, 1)));
		
		
		final Box redBox = new Box(0.75f, 0.75f, 0.10f);
		
		final Material yellowBoxMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		yellowBoxMat.setColor("Color", ColorRGBA.Yellow);
		
		final Geometry redBoxGeo = new Geometry("<TranslateGeo>", redBox);
		redBoxGeo.setLocalTranslation(0.0f, 0.0f, -1.0f);
		redBoxGeo.setMaterial(yellowBoxMat);
		node.attachChild(redBoxGeo);
		wrapplers.add(new Wrappler(redBoxGeo, new Vector3f(1, 1, 0)));
		
		
		final Box magentaBox = new Box(0.75f, 0.1f, 0.75f);
		
		final Material magentaBoxMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		magentaBoxMat.setColor("Color", ColorRGBA.Magenta);
		
		final Geometry magentaBoxGeo = new Geometry("<TranslateGeo>", magentaBox);
		magentaBoxGeo.setLocalTranslation(0.0f, -1.0f, 0.0f);
		magentaBoxGeo.setMaterial(magentaBoxMat);
		node.attachChild(magentaBoxGeo);
		wrapplers.add(new Wrappler(magentaBoxGeo, new Vector3f(1, 0, 1)));
		
		
		final Box cyanBox = new Box(0.1f, 0.75f, 0.75f);
		
		final Material cyanBoxMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		cyanBoxMat.setColor("Color", ColorRGBA.Cyan);
		
		final Geometry cyanBoxGeo = new Geometry("<TranslateGeo>", cyanBox);
		cyanBoxGeo.setLocalTranslation(-1.0f, 0.0f, 0.0f);
		cyanBoxGeo.setMaterial(cyanBoxMat);
		node.attachChild(cyanBoxGeo);
		wrapplers.add(new Wrappler(cyanBoxGeo, new Vector3f(0, 1, 1)));
		
		
		final Box whiteBox = new Box(0.5f, 0.5f, 0.5f);
		
		final Material whiteBoxMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		whiteBoxMat.setColor("Color", ColorRGBA.White);
		
		final Geometry whiteBoxGeo = new Geometry("<TranslateGeo>", whiteBox);
		whiteBoxGeo.setMaterial(whiteBoxMat);
		node.attachChild(whiteBoxGeo);
		wrapplers.add(new Wrappler(whiteBoxGeo, new Vector3f(1, 1, 1)));
		
		
		selectedElement.addValueChangedHandler(new ValueChangedHandler<Wrappler>() {
			@Override
			public void valueChanged(final Wrappler oldValue, final Wrappler newValue) {
				if(oldValue != null) {
					oldValue.setSelected(false);
				}
				if(newValue != null) {
					newValue.setSelected(true);
				}
			}
		});
		
		app.getInputManager().addListener(new ActionListener() {
			@Override
			public void onAction(final String name, final boolean isPressed, final float tpf) {
				if(isPressed) {
					if(selectedElement.isSet()) {
						final EditorCamera ec = app.getStateManager().getState(EditorCamera.class);
						if(ec != null) {
							ec.setEnabled(false);
						}
						
						final TempVars tmp = TempVars.get();
						try {
							final Vector2f click2d = getApplication().getInputManager().getCursorPosition();
							final Camera cam = getApplication().getCamera();
							final Vector3f click3d = cam.getWorldCoordinates(click2d, 0.0f, tmp.vect1);
							
							final Vector3f dirToTarget = tmp.vect2.set(click3d).subtractLocal(cam.getLocation()).normalizeLocal();
							dirToTarget.multLocal(clickedDistanceToTarget);
							
							clickedLocation = new Vector3f(tmp.vect3.set(cam.getLocation()).addLocal(dirToTarget));
							clickedValue = (Vector3f)mode.get();
							clickedNodeLocation = new Vector3f(node.getLocalTranslation());
							clickedCollisionsPointNodeDifference = new Vector3f(clickedLocation).subtractLocal(node.getLocalTranslation());
							
							debugGeo0.setLocalTranslation(clickedLocation);
						} finally {
							tmp.release();
						}
					}
				} else {
					if(clickedLocation != null) {
						final TempVars tmp = TempVars.get();
						try {
							final Vector2f click2d = getApplication().getInputManager().getCursorPosition();
							final Camera cam = getApplication().getCamera();
							final Vector3f click3d = cam.getWorldCoordinates(click2d, 0.0f, tmp.vect1);
							
							final Vector3f dirToTarget = tmp.vect2.set(click3d).subtractLocal(cam.getLocation()).normalizeLocal();
							dirToTarget.multLocal(clickedDistanceToTarget);
							
							final Vector3f target = tmp.vect3.set(cam.getLocation()).addLocal(dirToTarget);
							
							final Vector3f movedWay = tmp.vect4.set(target).subtractLocal(clickedCollisionsPointNodeDifference);
							final Vector3f moveDifference = tmp.vect5.set(movedWay).subtract(clickedNodeLocation);
							moveDifference.multLocal(selectedElement.source().getMoveAxes());
							
							if(model.getGridMode() == GridMode.MOVE_BY) {
								moveDifference.divideLocal(model.getGridSize());
								moveDifference.x = (float) Math.round(moveDifference.x);
								moveDifference.y = (float) Math.round(moveDifference.y);
								moveDifference.z = (float) Math.floor(moveDifference.z);
								moveDifference.multLocal(model.getGridSize());
							}
							
							final Vector3f targetLocation = tmp.vect7.set(clickedValue).addLocal(moveDifference);
							
							mode.createChangeCommand(null, clickedValue).getA().run();
							final Mapping<Runnable, Runnable> targetLocationCommand = mode.createChangeCommand(null, targetLocation);
							model.execute(targetLocationCommand);
							targetLocationCommand.getA().run();
						} finally {
							tmp.release();
						}
					}
					
					clickedLocation = null;
					
					final EditorCamera ec = app.getStateManager().getState(EditorCamera.class);
					if(ec != null) {
						ec.setEnabled(true);
					}
				}
			}
		}, "TranslateMousePressed");
		app.getInputManager().addMapping("TranslateMousePressed", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	}
	
	private void createDebug() {
		debugGeo0 = new Geometry("Debug0", new Box(0.05f, 0.05f, 0.05f));
		final Material debugGeo0Mat = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		debugGeo0Mat.setColor("Color", ColorRGBA.Orange);
		debugGeo0.setMaterial(debugGeo0Mat);
		//cursorViewportNode.attachChild(debugGeo0);
		
		debugGeo1 = new Geometry("Debug1", new Box(0.05f, 0.05f, 0.05f));
		final Material debugGeo1Mat = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		debugGeo1Mat.setColor("Color", ColorRGBA.Black);
		debugGeo1.setMaterial(debugGeo1Mat);
		//cursorViewportNode.attachChild(debugGeo1);
		
		debugGeo2 = new Geometry("Debug2", new Box(0.05f, 0.05f, 0.05f));
		final Material debugGeo2Mat = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		debugGeo2Mat.setColor("Color", ColorRGBA.Gray);
		debugGeo2.setMaterial(debugGeo2Mat);
		//cursorViewportNode.attachChild(debugGeo2);
	}
	
	@Override
	public void update(final float tpf) {
		cursorViewportCamera.setLocation(getApplication().getCamera().getLocation());
		cursorViewportCamera.setRotation(getApplication().getCamera().getRotation());
		
		if(clickedLocation == null) {
			updateNonClicked();
		} else {
			updateClicked();
		}
		
		node.setLocalTranslation((Vector3f) worldCoordinates.get());
	}
	public void updateNonClicked() {
		final Vector2f click2d = getApplication().getInputManager().getCursorPosition();
		
		final Camera cam = getApplication().getCamera();
		final Vector3f click3d = cam.getWorldCoordinates(click2d, 0.0f);
		final Vector3f dir = cam.getWorldCoordinates(click2d, 1.0f).subtract(click3d).normalizeLocal();
		
		final Ray ray = new Ray(click3d, dir);
		
		colResults.clear();
		
		boolean found = false;
		
		cursorViewportNode.collideWith(ray, colResults);
		for(int i = 0, size = colResults.size(); i < size && !found; i++) {
			final CollisionResult result = colResults.getCollision(i);
			final Geometry geo = result.getGeometry();
			if(geo.getName().equals("<TranslateGeo>")) {
				for(final Wrappler wrappler:wrapplers) {
					if(wrappler.hasGeometry(geo)) {
						selectedElement.sink(wrappler);
						clickedDistanceToTarget = result.getDistance();
						
						found = true;
						break;
					}
				}
			}
		}
		
		if(!found) {
			selectedElement.unset();
		}
	}
	public void updateClicked() {
		final TempVars tmp = TempVars.get();
		try {
			final Vector2f click2d = getApplication().getInputManager().getCursorPosition();
			final Camera cam = getApplication().getCamera();
			final Vector3f click3d = cam.getWorldCoordinates(click2d, 0.0f, tmp.vect1);
			
			final Vector3f dirToTarget = tmp.vect2.set(click3d).subtractLocal(cam.getLocation()).normalizeLocal();
			dirToTarget.multLocal(clickedDistanceToTarget);
			
			final Vector3f target = tmp.vect3.set(cam.getLocation()).addLocal(dirToTarget);
			
			debugGeo1.setLocalTranslation(target);
			
			final Vector3f movedWay = tmp.vect4.set(target).subtractLocal(clickedCollisionsPointNodeDifference);
			final Vector3f moveDifference = tmp.vect5.set(movedWay).subtractLocal(clickedNodeLocation);
			moveDifference.multLocal(selectedElement.source().getMoveAxes());
			
			if(model.getGridMode() == GridMode.MOVE_BY) {
				moveDifference.divideLocal(model.getGridSize());
				moveDifference.x = (float) Math.round(moveDifference.x);
				moveDifference.y = (float) Math.round(moveDifference.y);
				moveDifference.z = (float) Math.floor(moveDifference.z);
				moveDifference.multLocal(model.getGridSize());
			}
			
			final Vector3f finalTarget = tmp.vect7.set(clickedValue).add(moveDifference);
			debugGeo2.setLocalTranslation(finalTarget);
			model.executeTemporary(mode.createChangeCommand(null, finalTarget));
		} finally {
			tmp.release();
		}
	}
	
	
	@Override
	protected void onEnable() {
		cursorViewportNode.attachChild(node);
	}
	@Override
	protected void onDisable() {
		((SimpleApplication) getApplication()).getRootNode().detachChild(node);
	}
	
	@Override
	protected void cleanup(final Application app) {
		app.getInputManager().deleteMapping("TranslateMousePressed");
		app.getRenderManager().removeMainView("Vector3f cursor");
	}
	
	private static final class Wrappler {
		private final Spatial spatial;
		private final Vector3f moveAxes;
		
		private boolean selected;
		private ColorRGBA preSelectColor;
		
		public Wrappler(final Spatial spatial, final Vector3f moveAxes) {
			this.spatial = Requires.notNull(spatial, "spatial == null");
			this.moveAxes = Requires.notNull(moveAxes, "moveAxes == null");
		}
		public void setSelected(final boolean newSelected) {
			if(newSelected == selected) {
				return;
			}
			setSelected(newSelected, spatial);
			this.selected = newSelected;
		}
		private void setSelected(final boolean newSelected, final Spatial spatial) {
			if(spatial instanceof Geometry) {
				final Geometry geo = (Geometry) spatial;
				final Material mat = geo.getMaterial();
				if(newSelected) {
					preSelectColor = (ColorRGBA) mat.getParam("Color").getValue();
					mat.setColor("Color", ColorRGBA.Brown);
				} else {
					mat.setColor("Color", preSelectColor);
				}
				geo.setMaterial(mat);
			} else {
				final Node node = (Node) spatial;
				for(final Spatial child:node.getChildren()) {
					setSelected(newSelected, child);
				}
			}
		}
		public boolean hasGeometry(final Geometry geo) {
			return(hasGeometry(geo, spatial));
		}
		private boolean hasGeometry(final Geometry geo, final Spatial spatial) {
			if(spatial instanceof Geometry) {
				return(spatial.equals(geo));
			} else {
				final Node node = (Node) spatial;
				for(final Spatial child:node.getChildren()) {
					if(hasGeometry(geo, child)) {
						return(true);
					}
				}
			}
			return(false);
		}
		public Vector3f getMoveAxes() {
			return (moveAxes);
		}
	}
	
}
