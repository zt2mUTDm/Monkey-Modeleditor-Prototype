package core;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

import jme3utilities.InitialState;
import jme3utilities.SimpleAppState;

/**
 * App state to manage view-port updating.
 * <p>
 * Compare with RootNodeAppState, which does not allow sharing of scenes between
 * viewports.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class ViewportAppState extends SimpleAppState {
	// *************************************************************************
	// constants and loggers

	/**
	 * message logger for this class
	 */
	final private static Logger logger
	= Logger.getLogger(ViewportAppState.class.getName());
	// *************************************************************************
	// fields

	/**
	 * time interval between frames (in seconds, &ge;0)
	 */
	private float tpf;
	/**
	 * list of scene-graph root spatials to update
	 */
	final private List<Spatial> updateList = new ArrayList<>(10);
	// *************************************************************************
	// constructor

	/**
	 * Instantiate a disabled appstate.
	 */
	public ViewportAppState() {
		super(InitialState.Enabled);
	}
	// *************************************************************************
	// SimpleAppState methods

	/**
	 * Callback to perform rendering for this state during each frame.
	 *
	 * @param rm application's render manager (not null)
	 */
	@Override
	public void render(final RenderManager rm) {
		super.render(rm);

		updateList.clear();

		List<ViewPort> viewPorts = rm.getPreViews();
		addToUpdateList(viewPorts);

		viewPorts = rm.getMainViews();
		addToUpdateList(viewPorts);

		viewPorts = rm.getPostViews();
		addToUpdateList(viewPorts);

		for (final Spatial root : updateList) {
			root.updateLogicalState(tpf);
			root.updateGeometricState();
		}
	}

	/**
	 * Callback to update this state prior to rendering. (Invoked once per
	 * frame.)
	 *
	 * @param elapsedTime time interval between frames (in seconds, &ge;0)
	 */
	@Override
	public void update(final float elapsedTime) {
		super.update(elapsedTime);
		this.tpf = elapsedTime;
	}
	// *************************************************************************
	// private methods

	/**
	 * Add root spatials from the specified view ports to the update list.
	 *
	 * @param viewPortList list of view ports (not null, aliases created)
	 */
	private void addToUpdateList(final List<ViewPort> viewPortList) {
		for (final ViewPort vp : viewPortList) {
			if (vp.isEnabled()) {
				final List<Spatial> sceneList = vp.getScenes();
				for (final Spatial root : sceneList) {
					if (root != rootNode
							&& root != guiNode
							&& !"Physics Debug Root Node".equals(root.getName())
							&& !updateList.contains(root)) {
						updateList.add(root);
					}
				}
			}
		}
	}
}