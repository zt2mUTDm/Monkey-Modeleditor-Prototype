package core.editables;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Objects;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class AnimationControl extends AbstractControl {
	private Map<String, String> mappings;
	
	@Override
	public void write(final JmeExporter ex) throws IOException {
		super.write(ex);
		
		final OutputCapsule capsule = ex.getCapsule(this);
		
		if(mappings != null) {
			final String[][] m = new String[mappings.size()][2];
			int i = 0;
			for(final Entry<String, String> e:mappings.entrySet()) {
				final String[] arr = m[i++];
				arr[0] = e.getKey();
				arr[1] = e.getValue();
			}
			capsule.write(m, "mappings", new String[0][2]);
		}
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		super.read(im);
		
		final InputCapsule capsule = im.getCapsule(this);
		final String[][] m = capsule.readStringArray2D("mappings", new String[0][2]);
		for(final String[] arr:m) {
			addActionMapping(arr[0], arr[1]);
		}
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		final AnimationControl c = new AnimationControl();
		c.mappings = new HashMap<>(mappings);
		return(c);
	}
	
	@Override
	protected void controlUpdate(final float tpf) {
		
	}
	
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
	
	public void addActionMapping(final String internalName, final String actionName) {
		if(mappings == null) {
			mappings = new HashMap<>();
		}
		mappings.put(Requires.notNull(internalName, "internalName == null"), Requires.notNull(actionName, "actionName == null"));
	}
	
	public void removeActionMapping(final String internalName, final String actionName) {
		final String storedActionName = mappings.get(Requires.notNull(internalName, "internalName == null"));
		if(Objects.equal(storedActionName, Requires.notNull(actionName, "actionName == null"))) {
			mappings.remove(internalName);
			
			if(mappings.isEmpty()) {
				mappings = null;
			}
		}
	}
	
	public Map<String, String> getActionMappings() {
		return(mappings != null ? new HashMap<>(mappings) : new HashMap<>());
	}
	
}
