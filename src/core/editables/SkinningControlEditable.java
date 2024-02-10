package core.editables;

import java.util.ArrayList;
import java.util.List;

import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.scene.Node;

public final class SkinningControlEditable extends ControlEditable {
	private SkinningControlEditable(final SkinningControl control) {
		super(control);
	}
	
	public Node getAttachmentsNode(final String name) {
		return(getControl().getAttachmentsNode(name));
	}
	
	public List<Joint> getJointList() {
		return(new ArrayList<>(getControl().getArmature().getJointList()));
	}
	
	@Override
	public SkinningControl getControl() {
		return (SkinningControl) (super.getControl());
	}
	
	public static SkinningControlEditable valueOf(final SkinningControl control) {
		final SkinningControlEditable edit = new SkinningControlEditable(control);
		return(edit);
	}
	
}
