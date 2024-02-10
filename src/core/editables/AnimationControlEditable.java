package core.editables;

public final class AnimationControlEditable extends ControlEditable {
	private AnimationControlEditable(final AnimationControl control) {
		super(control);
	}
	
	public static AnimationControlEditable valueOf(final AnimationControl control) {
		return(new AnimationControlEditable(control));
	}
}
