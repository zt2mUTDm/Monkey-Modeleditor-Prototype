package core.editables;

import com.simsilica.mathd.Vec3d;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class PhysicsLocationDpEditionMode implements EditionMode {
	private final ValueIo<Vec3d> io;
	
	public PhysicsLocationDpEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Vec3d>() {
			@Override
			public Vec3d get() {
				return(parent.getPhysicsLocationDp());
			}
			@Override
			public void set(final Vec3d newValue) {
				parent.setPhysicsLocationDp(newValue);
			}
		};
	}
	public PhysicsLocationDpEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Vec3d>() {
			@Override
			public Vec3d get() {
				return(parent.getPhysicsLocationDp());
			}
			@Override
			public void set(final Vec3d newValue) {
				parent.setPhysicsLocationDp(newValue);
			}
		};
	}
	public PhysicsLocationDpEditionMode(final CharacterControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Vec3d>() {
			@Override
			public Vec3d get() {
				return(parent.getPhysicsLocationDp());
			}
			@Override
			public void set(final Vec3d newValue) {
				parent.setPhysicsLocationDp(newValue);
			}
		};
	}
	public PhysicsLocationDpEditionMode(final GhostControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Vec3d>() {
			@Override
			public Vec3d get() {
				return(parent.getPhysicsLocationDp());
			}
			@Override
			public void set(final Vec3d newValue) {
				parent.setPhysicsLocationDp(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Physics location DP");
	}
	
	@Override
	public Vec3d get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Vec3d cur = get();
		final Vec3d next = ((Vec3d) value);
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
