package core.editables;

import java.util.Collection;
import java.util.Collections;

import com.jme3.anim.AnimComposer;

import online.money_daisuki.api.base.Requires;

public final class AnimComposerActionEditable implements Editable {
	private final String name;
	private final AnimComposerEditable composer;
	
	public AnimComposerActionEditable(final String name, final AnimComposerEditable composer) {
		this.name = Requires.notNull(name, "name == null");
		this.composer = Requires.notNull(composer, "composer == null");
	}
	
	@Override
	public void setSelected(final boolean b) {
		final AnimComposer c = composer.getControl();
		
		if(b) {
			c.setCurrentAction(name);
		} else {
			c.reset();
		}
	}
	
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(Collections.emptyList());
	}
	
	@Override
	public String toString() {
		return(name);
	}
}
