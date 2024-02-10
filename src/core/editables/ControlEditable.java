package core.editables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public class ControlEditable implements Editable {
	private final Control control;
	private final List<EditionMode> modes;
	
	public ControlEditable(final Control control) {
		this.control = Requires.notNull(control, "control == null");
		this.modes = new ArrayList<>();
	}
	protected void addMode(final EditionMode mode) {
		this.modes.add(Requires.notNull(mode, "mode == null"));
	}
	
	public Control getControl() {
		return (control);
	}
	
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(new ArrayList<>(modes));
	}
	
	@Override
	public void setSelected(final boolean b) {
		
	}
	
	public Control createControl() {
		return(control);
	}
	
	@Override
	public String toString() {
		return(control.getClass().getName());
	}
	
	public static ControlEditable valueOf(final Control control) {
		return(new ControlEditable(control));
	}
}
