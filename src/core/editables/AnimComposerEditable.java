package core.editables;

import java.util.Collection;

import com.jme3.anim.AnimComposer;

public final class AnimComposerEditable extends ControlEditable {
	private AnimComposerEditable(final AnimComposer control) {
		super(control);
	}
	
	@Override
	public AnimComposer getControl() {
		return (AnimComposer) (super.getControl());
	}
	
	public Collection<String> getAnimationNames() {
		return(((AnimComposer) getControl()).getAnimClipsNames());
	}
	
	public static AnimComposerEditable valueOf(final AnimComposer control) {
		final AnimComposerEditable edit = new AnimComposerEditable(control);
		return(edit);
	}
	
}
