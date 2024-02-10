package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class KinematicEditionMode implements EditionMode {
	private final ValueIo<Boolean> io;
	
	public KinematicEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Boolean>() {
			@Override
			public Boolean get() {
				return(parent.isKinematic());
			}
			@Override
			public void set(final Boolean newValue) {
				parent.setKinematic(newValue);
			}
		};
	}
	public KinematicEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Boolean>() {
			@Override
			public Boolean get() {
				return(parent.isKinematic());
			}
			@Override
			public void set(final Boolean newValue) {
				parent.setKinematic(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Kinematic");
	}
	
	@Override
	public Boolean get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final boolean cur = io.get();
		final boolean next = ((Boolean) value);
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

