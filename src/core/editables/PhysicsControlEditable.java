package core.editables;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;

import online.money_daisuki.api.base.Requires;

public abstract class PhysicsControlEditable extends ControlEditable {
	private final CollisionShapeEditable shape;
	
	protected PhysicsControlEditable(final PhysicsControl control, final CollisionShapeEditable shape) {
		super(control);
		this.shape = Requires.notNull(shape, "shape == null");
	}
	protected PhysicsControlEditable(final VehicleControl control, final CollisionShapeEditable shape) {
		super(control);
		this.shape = Requires.notNull(shape, "shape == null");
	}
	
	public CollisionShapeEditable getShape() {
		return(shape);
	}
	
	public static PhysicsControlEditable valueOf(final PhysicsControl control) {
		if(control instanceof GhostControl) {
			return(GhostControlEditable.valuesOf((GhostControl)control));
		} else if(control instanceof RigidBodyControl) {
			return(RigidBodyControlEditable.valuesOf((RigidBodyControl)control));
		} else if(control instanceof CharacterControl) {
			return(CharacterControlEditable.valuesOf((CharacterControl)control));
		} else if(control instanceof VehicleControl) {
			return(VehicleControlEditable.valuesOf((VehicleControl)control));
		}
		
		throw new UnsupportedOperationException("");
	}
}
