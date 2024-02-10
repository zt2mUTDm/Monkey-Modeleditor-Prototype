package core.editables;

import com.jme3.math.Quaternion;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class PhysicsRotationEditionMode implements EditionMode {
	private final ValueIo<Quaternion> io;
	
	public PhysicsRotationEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Quaternion>() {
			@Override
			public Quaternion get() {
				return(parent.getPhysicsRotation());
			}
			@Override
			public void set(final Quaternion newValue) {
				parent.setPhysicsRotation(newValue);
			}
		};
	}
	public PhysicsRotationEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Quaternion>() {
			@Override
			public Quaternion get() {
				return(parent.getPhysicsRotation());
			}
			@Override
			public void set(final Quaternion newValue) {
				parent.setPhysicsRotation(newValue);
			}
		};
	}
	public PhysicsRotationEditionMode(final GhostControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Quaternion>() {
			@Override
			public Quaternion get() {
				return(parent.getPhysicsRotation());
			}
			@Override
			public void set(final Quaternion newValue) {
				parent.setPhysicsRotation(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Physics rotation");
	}
	
	@Override
	public Quaternion get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Quaternion cur = get();
		final Quaternion next = ((Quaternion) value);
		return(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				io.set(next);
			}
		}, new Runnable() {
			@Override
			public void run() {
				io.set(cur);
			}
		}));
	}
	
	@Override
	public boolean isEditableByTable() {
		return(true);
	}
	
	@Override
	public boolean isEditableByThreeDView() {
		return(false);
	}
	
	@Override
	public EditionState createEditionState(final EditionStateModel model) {
		throw new UnsupportedOperationException();
	}
}
