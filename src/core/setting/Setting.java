package core.setting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.ValueChangedHandler;
import online.money_daisuki.api.io.json.DefaultJsonMap;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.io.json.MutableJsonMap;

public final class Setting {
	private SpecificSetting model;
	private ValueChangedHandler<Object> modelListener;
	
	private File file;
	private boolean autocommit;
	
	public Setting(final SpecificSetting parent) {
		setModel(parent);
		
		this.autocommit = false;
		//this.recentlyOpened = new String[0];
		
		parent.setDefaults();
	}
	
	public void setModel(final SpecificSetting newModel) {
		if(model != null) {
			model.removeValueChangeListener(modelListener);
			modelListener = null;
		}
		
		if(newModel != null) {
			modelListener = new ValueChangedHandler<Object>() {
				@Override
				public void valueChanged(final Object old, final Object nevv) {
					mayAutocommit();
				}
			};
			newModel.addValueChangeListener(modelListener);
			this.model = newModel;
		}
	}
	public SpecificSetting getModel() {
		return (model);
	}
	@SuppressWarnings("unchecked")
	public <T extends SpecificSetting> T getModel(final Class<T> c) {
		if(model == null) {
			return(null);
		}
		
		if(c.isAssignableFrom(model.getClass())) {
			return((T)model);
		} else {
			throw new ClassCastException();
		}
	}
	
	public void load(final File f) {
		load(f, true);
	}
	public void load(final File f, final boolean persistentFile) {
		try(final Reader in = new FileReader(f)) {
			final JsonDecoder dec = new JsonDecoder(in);
			final JsonMap map = dec.decode().asMap();
			
			model.load(map);
			
			if(persistentFile) {
				this.file = f;
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setAutocommit(final boolean autocommit) {
		this.autocommit = Requires.notNull(autocommit, "autocommit == null");
	}
	public boolean isAutocommiting() {
		return (autocommit);
	}
	
	public void setFile(final File file) {
		this.file = Requires.notNull(file, "file == null");
	}
	public void unsetFile() {
		this.file = null;
	}
	
	private void mayAutocommit() {
		if(model != null && file != null && autocommit) {
			commit();
		}
	}
	public void commit() {
		if(file == null) {
			throw new IllegalStateException("File not set");
		}
		
		final MutableJsonMap map = new DefaultJsonMap();
		model.save(map);
		
		try(final Writer out = new FileWriter(file)) {
			out.write(map.toJsonString());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
