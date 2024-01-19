package core.setting;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;

import com.google.common.base.Objects;

import core.GridMode;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.ValueChangedHandler;
import online.money_daisuki.api.io.json.DefaultJsonList;
import online.money_daisuki.api.io.json.JsonIntDataElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.io.json.JsonStringDataElement;
import online.money_daisuki.api.io.json.MutableJsonList;
import online.money_daisuki.api.io.json.MutableJsonMap;

public final class ModelSetting implements SpecificSetting {
	private final Collection<ValueChangedHandler<? super Object>> listeners;
	
	private String lastOpenedFolderPath;
	private String lastLoadedModelPath;
	
	private GridMode gridMode;
	private int gridSize;
	
	private Point topMenuWindowLocation;
	private Dimension topMenuWindowSize;
	
	private Point sceneGraphWindowLocation;
	private Dimension sceneGraphWindowSize;
	
	private Point propertiesWindowLocation;
	private Dimension propertiesWindowSize;
	
	private Point filesWindowLocation;
	private Dimension filesWindowSize;
	
	private Point cameraWindowLocation;
	private Dimension cameraWindowSize;
	
	private Point threeDTransformWindowLocation;
	private Dimension threeDTransformWindowSize;
	
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
	
	
	public void setTopWindowLocation(final Point topWindowMenuLocation) {
		if(!Objects.equal(this.topMenuWindowLocation, topWindowMenuLocation)) {
			final Point old = this.topMenuWindowLocation;
			this.topMenuWindowLocation = new Point(Requires.notNull(topWindowMenuLocation, "topWindowLocation == null"));
			fireValueChangeListener(old, topWindowMenuLocation);
		}
	}
	public Point getTopMenuWindowLocation() {
		return(topMenuWindowLocation);
	}
	
	public void setTopWindowSize(final Dimension topWindowMenuSize) {
		if(!Objects.equal(this.topMenuWindowSize, topWindowMenuSize)) {
			final Dimension old = this.topMenuWindowSize;
			this.topMenuWindowSize = new Dimension(Requires.notNull(topWindowMenuSize, "topWindowSize == null"));
			fireValueChangeListener(old, topWindowMenuSize);
		}
	}
	public Dimension getTopMenuWindowSize() {
		return(topMenuWindowSize);
	}
	
	public void setSceneGraphWindowLocation(final Point sceneGraphWindowLocation) {
		if(!Objects.equal(this.sceneGraphWindowLocation, sceneGraphWindowLocation)) {
			final Point old = this.sceneGraphWindowLocation;
			this.sceneGraphWindowLocation = new Point(Requires.notNull(sceneGraphWindowLocation, "sceneGraphWindowLocation == null"));
			fireValueChangeListener(old, sceneGraphWindowLocation);
		}
	}
	public Point getSceneGraphWindowLocation() {
		return(sceneGraphWindowLocation);
	}
	
	public void setSceneGraphWindowSize(final Dimension sceneGraphWindowSize) {
		if(!Objects.equal(this.sceneGraphWindowSize, sceneGraphWindowSize)) {
			final Dimension old = this.sceneGraphWindowSize;
			this.sceneGraphWindowSize = new Dimension(Requires.notNull(sceneGraphWindowSize, "sceneGraphWindowSize == null"));
			fireValueChangeListener(old, sceneGraphWindowSize);
		}
	}
	public Dimension getSceneGraphWindowSize() {
		return(sceneGraphWindowSize);
	}
	
	public void setPropertiesWindowLocation(final Point propertiesWindowLocation) {
		if(!Objects.equal(this.propertiesWindowLocation, propertiesWindowLocation)) {
			final Point old = this.propertiesWindowLocation;
			this.propertiesWindowLocation = new Point(Requires.notNull(propertiesWindowLocation, "propertiesWindowLocation == null"));
			fireValueChangeListener(old, propertiesWindowLocation);
		}
	}
	public Point getPropertiesWindowLocation() {
		return(propertiesWindowLocation);
	}
	
	public void setPropertiesWindowSize(final Dimension propertiesWindowSize) {
		if(!Objects.equal(this.propertiesWindowSize, propertiesWindowSize)) {
			final Dimension old = this.propertiesWindowSize;
			this.propertiesWindowSize = new Dimension(Requires.notNull(propertiesWindowSize, "propertiesWindowSize == null"));
			fireValueChangeListener(old, propertiesWindowSize);
		}
	}
	public Dimension getPropertiesWindowSize() {
		return(propertiesWindowSize);
	}
	
	public void setFilesWindowLocation(final Point filesWindowLocation) {
		if(!Objects.equal(this.filesWindowLocation, filesWindowLocation)) {
			final Point old = this.filesWindowLocation;
			this.filesWindowLocation = new Point(Requires.notNull(filesWindowLocation, "filesWindowLocation == null"));
			fireValueChangeListener(old, filesWindowLocation);
		}
	}
	public Point getFilesWindowLocation() {
		return(filesWindowLocation);
	}
	
	public void setFilesWindowSize(final Dimension filesWindowSize) {
		if(!Objects.equal(this.filesWindowSize, filesWindowSize)) {
			final Dimension old = this.filesWindowSize;
			this.filesWindowSize = new Dimension(Requires.notNull(filesWindowSize, "filesWindowSize == null"));
			fireValueChangeListener(old, filesWindowSize);
		}
	}
	public Dimension getFilesWindowSize() {
		return(filesWindowSize);
	}
	
	public void setCameraWindowLocation(final Point cameraWindowLocation) {
		if(!Objects.equal(this.cameraWindowLocation, cameraWindowLocation)) {
			final Point old = this.cameraWindowLocation;
			this.cameraWindowLocation = new Point(Requires.notNull(cameraWindowLocation, "cameraWindowLocation == null"));
			fireValueChangeListener(old, cameraWindowLocation);
		}
	}
	public Point getCameraWindowLocation() {
		return(cameraWindowLocation);
	}
	
	public void setCameraWindowSize(final Dimension cameraWindowSize) {
		if(!Objects.equal(this.cameraWindowSize, cameraWindowSize)) {
			final Dimension old = this.cameraWindowSize;
			this.cameraWindowSize = new Dimension(Requires.notNull(cameraWindowSize, "cameraWindowSize == null"));
			fireValueChangeListener(old, cameraWindowSize);
		}
	}
	public Dimension getCameraWindowSize() {
		return(cameraWindowSize);
	}
	
	public void setThreeDTransformWindowLocation(final Point threeDTransformWindowLocation) {
		if(!Objects.equal(this.threeDTransformWindowLocation, threeDTransformWindowLocation)) {
			final Point old = this.threeDTransformWindowLocation;
			this.threeDTransformWindowLocation = new Point(Requires.notNull(threeDTransformWindowLocation, "threeDTransformWindowLocation == null"));
			fireValueChangeListener(old, threeDTransformWindowLocation);
		}
	}
	public Point getThreeDTransformWindowLocation() {
		return(threeDTransformWindowLocation);
	}
	
	public void setThreeDTransformWindowSize(final Dimension threeDTransformWindowSize) {
		if(!Objects.equal(this.threeDTransformWindowSize, threeDTransformWindowSize)) {
			final Dimension old = this.threeDTransformWindowSize;
			this.threeDTransformWindowSize = new Dimension(Requires.notNull(threeDTransformWindowSize, "threeDTransformWindowSize == null"));
			fireValueChangeListener(old, threeDTransformWindowSize);
		}
	}
	public Dimension getThreeDTransformWindowSize() {
		return(threeDTransformWindowSize);
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
		
		if(map.containsKey("topMenuWindowLocation")) {
			final JsonList list = map.get("topMenuWindowLocation").asList();
			topMenuWindowLocation = new Point(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("topMenuWindowSize")) {
			final JsonList list = map.get("topMenuWindowSize").asList();
			topMenuWindowSize = new Dimension(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("sceneGraphWindowLocation")) {
			final JsonList list = map.get("sceneGraphWindowLocation").asList();
			sceneGraphWindowLocation = new Point(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("sceneGraphWindowSize")) {
			final JsonList list = map.get("sceneGraphWindowSize").asList();
			sceneGraphWindowSize = new Dimension(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("propertiesWindowLocation")) {
			final JsonList list = map.get("propertiesWindowLocation").asList();
			propertiesWindowLocation = new Point(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("propertiesWindowSize")) {
			final JsonList list = map.get("propertiesWindowSize").asList();
			propertiesWindowSize = new Dimension(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("filesWindowLocation")) {
			final JsonList list = map.get("filesWindowLocation").asList();
			filesWindowLocation = new Point(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("filesWindowSize")) {
			final JsonList list = map.get("filesWindowSize").asList();
			filesWindowSize = new Dimension(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("cameraWindowLocation")) {
			final JsonList list = map.get("cameraWindowLocation").asList();
			cameraWindowLocation = new Point(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("cameraWindowSize")) {
			final JsonList list = map.get("cameraWindowSize").asList();
			cameraWindowSize = new Dimension(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("threeDTransformWindowLocation")) {
			final JsonList list = map.get("threeDTransformWindowLocation").asList();
			threeDTransformWindowLocation = new Point(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
		
		if(map.containsKey("threeDTransformWindowSize")) {
			final JsonList list = map.get("threeDTransformWindowSize").asList();
			threeDTransformWindowSize = new Dimension(
					list.get(0).asData().asNumber().asBigDecimal().intValueExact(),
					list.get(1).asData().asNumber().asBigDecimal().intValueExact()
			);
		}
	}
	@Override
	public void save(final MutableJsonMap map) {
		map.put("lastOpenedFolder", new JsonStringDataElement(lastOpenedFolderPath));
		map.put("lastLoadedModelPath", new JsonStringDataElement(lastLoadedModelPath));
		map.put("gridMode", new JsonStringDataElement(gridMode.name()));
		map.put("gridSize", new JsonIntDataElement(gridSize));
		
		final MutableJsonList topMenuWindowLocation = new DefaultJsonList();
		topMenuWindowLocation.add(new JsonIntDataElement(this.topMenuWindowLocation.x));
		topMenuWindowLocation.add(new JsonIntDataElement(this.topMenuWindowLocation.y));
		map.put("topMenuWindowLocation", topMenuWindowLocation);
		
		final MutableJsonList topMenuWindowSize = new DefaultJsonList();
		topMenuWindowSize.add(new JsonIntDataElement(this.topMenuWindowSize.width));
		topMenuWindowSize.add(new JsonIntDataElement(this.topMenuWindowSize.height));
		map.put("topMenuWindowSize", topMenuWindowSize);
		
		final MutableJsonList sceneGraphWindowLocation = new DefaultJsonList();
		sceneGraphWindowLocation.add(new JsonIntDataElement(this.sceneGraphWindowLocation.x));
		sceneGraphWindowLocation.add(new JsonIntDataElement(this.sceneGraphWindowLocation.y));
		map.put("sceneGraphWindowLocation", sceneGraphWindowLocation);
		
		final MutableJsonList sceneGraphWindowSize = new DefaultJsonList();
		sceneGraphWindowSize.add(new JsonIntDataElement(this.sceneGraphWindowSize.width));
		sceneGraphWindowSize.add(new JsonIntDataElement(this.sceneGraphWindowSize.height));
		map.put("sceneGraphWindowSize", sceneGraphWindowSize);
		
		final MutableJsonList propertiesWindowLocation = new DefaultJsonList();
		propertiesWindowLocation.add(new JsonIntDataElement(this.propertiesWindowLocation.x));
		propertiesWindowLocation.add(new JsonIntDataElement(this.propertiesWindowLocation.y));
		map.put("propertiesWindowLocation", propertiesWindowLocation);
		
		final MutableJsonList propertiesWindowSize = new DefaultJsonList();
		propertiesWindowSize.add(new JsonIntDataElement(this.propertiesWindowSize.width));
		propertiesWindowSize.add(new JsonIntDataElement(this.propertiesWindowSize.height));
		map.put("propertiesWindowSize", propertiesWindowSize);
		
		final MutableJsonList filesWindowLocation = new DefaultJsonList();
		filesWindowLocation.add(new JsonIntDataElement(this.filesWindowLocation.x));
		filesWindowLocation.add(new JsonIntDataElement(this.filesWindowLocation.y));
		map.put("filesWindowLocation", filesWindowLocation);
		
		final MutableJsonList filesWindowSize = new DefaultJsonList();
		filesWindowSize.add(new JsonIntDataElement(this.filesWindowSize.width));
		filesWindowSize.add(new JsonIntDataElement(this.filesWindowSize.height));
		map.put("filesWindowSize", filesWindowSize);
		
		final MutableJsonList cameraWindowLocation = new DefaultJsonList();
		cameraWindowLocation.add(new JsonIntDataElement(this.cameraWindowLocation.x));
		cameraWindowLocation.add(new JsonIntDataElement(this.cameraWindowLocation.y));
		map.put("cameraWindowLocation", cameraWindowLocation);
		
		final MutableJsonList cameraWindowSize = new DefaultJsonList();
		cameraWindowSize.add(new JsonIntDataElement(this.cameraWindowSize.width));
		cameraWindowSize.add(new JsonIntDataElement(this.cameraWindowSize.height));
		map.put("cameraWindowSize", cameraWindowSize);
		
		final MutableJsonList threeDTransformWindowLocation = new DefaultJsonList();
		threeDTransformWindowLocation.add(new JsonIntDataElement(this.threeDTransformWindowLocation.x));
		threeDTransformWindowLocation.add(new JsonIntDataElement(this.threeDTransformWindowLocation.y));
		map.put("threeDTransformWindowLocation", threeDTransformWindowLocation);
		
		final MutableJsonList threeDTransformWindowSize = new DefaultJsonList();
		threeDTransformWindowSize.add(new JsonIntDataElement(this.threeDTransformWindowSize.width));
		threeDTransformWindowSize.add(new JsonIntDataElement(this.threeDTransformWindowSize.height));
		map.put("threeDTransformWindowSize", threeDTransformWindowSize);
	}
	@Override
	public void setDefaults() {
		this.lastOpenedFolderPath = System.getProperty("user.home");
		this.lastLoadedModelPath = System.getProperty("user.home");
		this.gridMode = GridMode.NONE;
		this.gridSize = 100;
		
		this.topMenuWindowLocation = new Point(0, 0);
		this.topMenuWindowSize = new Dimension(0, 0);
		
		this.sceneGraphWindowLocation = new Point(0, 0);
		this.sceneGraphWindowSize = new Dimension(0, 0);
		
		this.propertiesWindowLocation = new Point(0, 0);
		this.propertiesWindowSize = new Dimension(0, 0);
		
		this.filesWindowLocation = new Point(0, 0);
		this.filesWindowSize = new Dimension(0, 0);
		
		this.cameraWindowLocation = new Point(0, 0);
		this.cameraWindowSize = new Dimension(0, 0);
		
		this.threeDTransformWindowLocation = new Point(0, 0);
		this.threeDTransformWindowSize = new Dimension(0, 0);
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
