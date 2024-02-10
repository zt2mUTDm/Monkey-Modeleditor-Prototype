package core.editables;

import java.util.Collection;
import java.util.Collections;

import online.money_daisuki.api.base.Requires;

public final class AnimationControlMappingEditable implements Editable {
	private final String internalName;
	private final String modelName;
	
	public AnimationControlMappingEditable(final String internalName, final String modelName) {
		this.internalName = Requires.notNull(internalName, "internalName == null");
		this.modelName = Requires.notNull(modelName, "modelName == null");
	}
	
	@Override
	public void setSelected(final boolean b) {
		
	}
	
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(Collections.emptyList());
	}
	
	public String getInternalName() {
		return (internalName);
	}
	
	public String getModelName() {
		return (modelName);
	}
	
	@Override
	public String toString() {
		return(internalName + ": " + modelName);
	}
}
