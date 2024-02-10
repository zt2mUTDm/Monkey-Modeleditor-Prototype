package core.editables;

import com.simsilica.mathd.Quatd;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class PhysicsRotationDpEditionMode implements EditionMode {
	private final ValueIo<Quatd> io;
	
	public PhysicsRotationDpEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Quatd>() {
			@Override
			public Quatd get() {
				return(parent.getPhysicsRotationDp());
			}
			@Override
			public void set(final Quatd newValue) {
				parent.setPhysicsRotationDp(newValue);
			}
		};
	}
	public PhysicsRotationDpEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Quatd>() {
			@Override
			public Quatd get() {
				return(parent.getPhysicsRotationDp());
			}
			@Override
			public void set(final Quatd newValue) {
				parent.setPhysicsRotationDp(newValue);
			}
		};
	}
	public PhysicsRotationDpEditionMode(final GhostControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Quatd>() {
			@Override
			public Quatd get() {
				return(parent.getPhysicsRotationDp());
			}
			@Override
			public void set(final Quatd newValue) {
				parent.setPhysicsRotationDp(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Physics rotation DP");
	}
	
	@Override
	public Quatd get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Quatd cur = get();
		final Quatd next = ((Quatd) value);
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
