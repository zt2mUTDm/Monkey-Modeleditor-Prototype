package core;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.BorderLayout.Position;
import com.simsilica.lemur.core.AbstractGuiControlListener;
import com.simsilica.lemur.core.GuiControl;

import online.money_daisuki.api.base.Requires;

public final class ScrollPane extends Panel {
	private final Panel viewport;
	private final Panel viewportPanel;
	
	public ScrollPane(final Panel viewport) {
		this.viewport = Requires.notNull(viewport, "viewport == null");
		
		final BorderLayout layout = new BorderLayout();
		getControl(GuiControl.class).setLayout(layout);
		
		layout.addChild(new Slider(Axis.X), BorderLayout.Position.South);
		layout.addChild(new Slider(Axis.Y), BorderLayout.Position.East);
		
		viewportPanel = layout.addChild(new Panel(), Position.Center);
		
		viewportPanel.getControl(GuiControl.class).addListener(new AbstractGuiControlListener() {
			@Override
			public void reshape(final GuiControl source, final Vector3f pos, final Vector3f size) {
				System.out.println(source + " - " + pos + " - " + size + " - " + viewport.getWorldTranslation());
			}
		});
	}
	public Panel getViewportPanel() {
		return (viewportPanel);
	}
	public Panel getViewport() {
		return(viewport);
	}
}
