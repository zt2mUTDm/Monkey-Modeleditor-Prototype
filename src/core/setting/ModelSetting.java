package core.setting;

import java.util.Collection;
import java.util.LinkedList;

import com.google.common.base.Objects;

import core.GridMode;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.ValueChangedHandler;
import online.money_daisuki.api.io.json.JsonIntDataElement;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.io.json.JsonStringDataElement;
import online.money_daisuki.api.io.json.MutableJsonMap;

public final class ModelSetting implements SpecificSetting {
	private final Collection<ValueChangedHandler<? super Object>> listeners;
	
	private String lastOpenedFolderPath;
	private String lastLoadedModelPath;
	private GridMode gridMode;
	private int gridSize;
	
	public ModelSetting() {
		listeners = new LinkedList<>();
	}
	public void setLastOpenedFolderPath(final String lastOpenedFolder) {
		if(!Objects.equal(this.lastOpenedFolderPath, lastOpenedFolder)) {
			final String old = this.lastOpenedFolderPath;
			this.lastOpenedFolderPath = Requires.notNull(lastOpenedFolder, "lastOpenedFolder == null");
			fireValueChangeListener(old, lastOpenedFolderPath);
		}
	}
	public String getLastOpenedFolderPath() {
		return (lastOpenedFolderPath);
	}
	
	public void setLastLoadedModelPath(final String lastLoadedModelPath) {
		if(!Objects.equal(this.lastLoadedModelPath, lastLoadedModelPath)) {
			final String old = this.lastLoadedModelPath;
			this.lastLoadedModelPath = Requires.notNull(lastLoadedModelPath, "lastLoadedModelPath == null");
			fireValueChangeListener(old, lastLoadedModelPath);
		}
	}
	public String getLastLoadedModelPath() {
		return (lastLoadedModelPath);
	}
	
	public void setGridMode(final GridMode gridMode) {
		if(!Objects.equal(this.gridMode, gridMode)) {
			final GridMode old = this.gridMode;
			this.gridMode = Requires.notNull(gridMode, "gridMode == null");
			fireValueChangeListener(old, gridMode);
		}
	}
	public GridMode getGridMode() {
		return(gridMode);
	}
	
	public void setGridSize(final int gridSize) {
		if(!Objects.equal(this.gridSize, gridSize)) {
			final int old = this.gridSize;
			this.gridSize = gridSize;
			fireValueChangeListener(old, gridSize);
		}
	}
	public int getGridSize() {
		return(gridSize);
	}
	
	@Override
	public void load(final JsonMap map) {
		// Don't use methods to don't fire listeners on load
		if(map.containsKey("lastOpenedFolder")) {
			lastOpenedFolderPath = map.get("lastOpenedFolder").asData().asString();
		}
		
		if(map.containsKey("lastLoadedModelPath")) {
			lastLoadedModelPath = map.get("lastLoadedModelPath").asData().asString();
		}
		
		if(map.containsKey("gridMode")) {
			gridMode = GridMode.valueOf(map.get("gridMode").asData().asString());
		}
		
		if(map.containsKey("gridSize")) {
			gridSize = map.get("gridSize").asData().asNumber().asBigInteger().intValueExact();
		}
	}
	@Override
	public void save(final MutableJsonMap map) {
		map.put("lastOpenedFolder", new JsonStringDataElement(lastOpenedFolderPath));
		map.put("lastLoadedModelPath", new JsonStringDataElement(lastLoadedModelPath));
		map.put("gridMode", new JsonStringDataElement(gridMode.name()));
		map.put("gridSize", new JsonIntDataElement(gridSize));
	}
	@Override
	public void setDefaults() {
		this.lastOpenedFolderPath = System.getProperty("user.home");
		this.lastLoadedModelPath = System.getProperty("user.home");
		this.gridMode = GridMode.NONE;
		this.gridSize = 100;
	}
	
	@Override
	public void addValueChangeListener(final ValueChangedHandler<? super Object> l) {
		listeners.add(l);
	}
	@Override
	public void removeValueChangeListener(final ValueChangedHandler<? super Object> l) {
		listeners.remove(l);
	}
	private void fireValueChangeListener(final Object oldValue, final Object newValue) {
		for(final ValueChangedHandler<? super Object> l:listeners) {
			l.valueChanged(oldValue, newValue);
		}
	}
}
