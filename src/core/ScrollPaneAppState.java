package core;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.simsilica.lemur.core.GuiControl;

public final class ScrollPaneAppState extends BaseAppState {
	private final ScrollPane pane;
	
	public ScrollPaneAppState(final ScrollPane pane) {
		this.pane = pane;
	}
	
	@Override
	protected void initialize(final Application app) {
		final Camera cam = app.getGuiViewPort().getCamera().clone();
		cam.setViewPort(0.5f , 1.0f  ,  0.0f , 0.5f);
		
		final ViewPort viewport = app.getRenderManager().createMainView("ScrollPane@" + pane.hashCode(), cam);
		viewport.attachScene(pane.getViewport());
		viewport.setClearFlags(true, true, true);
		viewport.setBackgroundColor(ColorRGBA.Blue);
		
		pane.getViewport().getControl(GuiControl.class).setSize(new Vector3f(500, 500, 1));
		
		
	}
	
	@Override
	protected void cleanup(final Application app) {
		
	}
	
	@Override
	protected void onEnable() {
		
	}
	
	@Override
	protected void onDisable() {
		
	}
	
}
