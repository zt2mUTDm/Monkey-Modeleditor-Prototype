package core.ui.swing;

import java.util.Collection;
import java.util.Collections;

import com.jme3.animation.AnimControl;

import core.editables.AnimControlEditable;
import core.editables.Editable;
import core.editables.EditionMode;
import online.money_daisuki.api.base.Requires;

@SuppressWarnings("deprecation")
public final class AnimControlActionEditable implements Editable {
	private final String name;
	private final AnimControlEditable control;
	
	public AnimControlActionEditable(final String name, final AnimControlEditable control) {
		this.name = Requires.notNull(name, "name == null");
		this.control = Requires.notNull(control, "control == null");
	}
	
	@Override
	public void setSelected(final boolean b) {
		final AnimControl c = (AnimControl) control.getControl();
		if(b) {
			c.createChannel().setAnim(name);
		} else {
			c.clearChannels();
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
