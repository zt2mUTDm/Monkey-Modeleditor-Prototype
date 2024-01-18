package core;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JOptionPane;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;

import core.setting.ModelSetting;
import core.setting.Setting;
import jme3utilities.ViewPortAppState;
import online.money_daisuki.api.base.ArgumentHandler;
import online.money_daisuki.api.base.ArgumentParserResult;

public class Main {
	public static void main(final String[] args) {
		final ArgumentHandler handler = new ArgumentHandler(args);
		handler.addSwitch("config", 1);
		handler.addSwitch("c", 1);
		handler.addSwitch("no-config", 0);
		
		handler.addAlias("c", "config");
		handler.addConflicts("config", "no-config");
		
		handler.setHelpText(
				"Usage:\n"
				+ "java [VM_OPTIONS] -jar <filename>.jar [PROGRAM_OPTIONS]\n"
				+ "javaw [VM_OPTIONS] -jar <filename>.jar [PROGRAM_OPTIONS]\n"
				+ "\n"
				+ "PROGRAM_OPTIONS:\n"
				+ "\n"
				+ "\t-c \t--config <file>                  \tSpecific the config file.\n"
				+ "\t   \t--no-config <file>               \tDon't use a config file.\n"
				+ "\t   \t--help                           \tShow this help text and exit.\n"
				+ "\n"
				+ "Consult \"java -help\" for VM_OPTIONS.",
				"help");
		
		final ArgumentParserResult result = handler.source();
		
		final Setting setting = loadSetting(result);
		final SimpleApplication app = new SimpleApplication() {
			@Override
			public void simpleInitApp() {
				final InputManager input = getInputManager();
				input.deleteMapping("SIMPLEAPP_Exit");
				
				getFlyByCamera().setMoveSpeed(15f);
				getFlyByCamera().setDragToRotate(true);
				
				setPauseOnLostFocus(false);
				
				final BulletAppState state = new BulletAppState();
				state.setDebugEnabled(true);
				getStateManager().attach(state);
				
				getStateManager().attach(new ViewPortAppState());
				final SimpleApplication myself = this;
				
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						final ModelEditorUi ui = new ModelEditorUi(myself, setting.getModel(ModelSetting.class));
						ui.show();
					}
				});
			}
		};
		app.setPauseOnLostFocus(false);
		app.start();
	}
	
	private static Setting loadSetting(final ArgumentParserResult result) {
		final Setting setting = new Setting(new ModelSetting());
		if(result.containsSwitch("config")) {
			final String configPath = result.getSwitchArguments("config")[0];
			final File configFile = new File(configPath);
			
			if(!configFile.exists() && !createSettingFile(configFile)) {
				return(setting);
			}
			
			setting.load(configFile, true);
			setting.setAutocommit(true);
			return(setting);
		} else if(result.containsSwitch("no-config")) {
			return(setting);
		} else {
			final File configFile = new File(System.getProperty("user.home") + "/.unnamedmodeleditor");
			if(!configFile.exists() && !createSettingFile(configFile)) {
				return(setting);
			}
			setting.load(configFile, true);
			setting.setAutocommit(true);
			return(setting);
		}
	}
	private static boolean createSettingFile(final File f) {
		try {
			if(!f.createNewFile()) {
				JOptionPane.showMessageDialog(null, "Could not create setting file " + f.getAbsolutePath());
				return(false);
			}
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(null, "Could not create setting file " + f.getAbsolutePath());
			return(false);
		}
		
		try(final Writer out = new FileWriter(f)) {
			out.write("{}\n");
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(null, "Could not create setting file " + f.getAbsolutePath());
			return(false);
		}
		return(true);
	}
}
