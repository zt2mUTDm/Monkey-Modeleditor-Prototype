package core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.core.GuiControl;
import com.simsilica.lemur.core.GuiLayout;
import com.simsilica.lemur.style.StyleLoader;

public final class ScrollMain {
	public static void main(final String[] args) {
		final SimpleApplication app = new SimpleApplication() {
			@Override
			public void simpleInitApp() {
				getStateManager().attach(new ViewportAppState());
				
				build(this, settings);
			}
		};
		app.start();
	}
	private static void build(final SimpleApplication app, final AppSettings appSettings) {
		GuiGlobals.initialize(app);
		//BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
		
		final URL url = GuiGlobals.class.getResource("/com/simsilica/lemur/style/base/glass-styles.groovy");
		try(final Reader in = new InputStreamReader(url.openStream())) {
			new StyleLoader().loadStyle(url.toString(), in);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		
		final Container myWindow = new Container(new BorderLayout());
		app.getGuiNode().attachChild(myWindow);
		
		myWindow.setLocalTranslation(0, appSettings.getHeight(), 0);
		
		// Add some elements
		final Button clickMe = myWindow.addChild(new Button("Add"), BorderLayout.Position.North);
		clickMe.addClickCommands(new Command<Button>() {
			@Override
			public void execute( final Button source ) {
				System.out.println("The world is yours.");
			}
		});
		
		
		
		final Container viewport = new Container((GuiLayout)null);
		
		viewport.getControl(GuiControl.class).setSize(new Vector3f(500, 60, 1));
		
		//final ScrollPane container = myWindow.addChild(new ScrollPane(viewport), BorderLayout.Position.Center);
		//app.getStateManager().attach(new ScrollPaneAppState(container));
		
		//container.addChild(new Button("A")).setLocalTranslation(30, 10, 0);
		final ProgressBar progressBar = viewport.addChild(new ProgressBar(new DefaultRangedValueModel(0, 100, 50)));
		progressBar.setLocalTranslation(100, 10, 0);
		progressBar.setMessage("Abc");
		progressBar.getControl(GuiControl.class).setSize(new Vector3f(500, 60, 1));
		
		//app.getGuiNode().attachChild(viewport);
		
		//container.getControl(GuiControl.class).setLayout(null);
	}
}
