package core.editables;

import java.util.Collection;

import com.jme3.animation.AnimControl;

@SuppressWarnings("deprecation")
public final class AnimControlEditable extends ControlEditable {
	private AnimControlEditable(final AnimControl control) {
		super(control);
	}
	
	public Collection<String> getAnimationNames() {
		return(((AnimControl) getControl()).getAnimationNames());
	}
	
	public static AnimControlEditable valueOf(final AnimControl control) {
		final AnimControlEditable edit = new AnimControlEditable(control);
		return(edit);
	}
	
}
