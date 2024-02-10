package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class ContactDampingEditionMode implements EditionMode {
	private final ValueIo<Float> io;
	
	public ContactDampingEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getContactDamping());
			}
			@Override
			public void set(final Float newValue) {
				parent.setContactDamping(newValue);
			}
		};
	}
	public ContactDampingEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getContactDamping());
			}
			@Override
			public void set(final Float newValue) {
				parent.setContactDamping(newValue);
			}
		};
	}
	public ContactDampingEditionMode(final CharacterControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getContactDamping());
			}
			@Override
			public void set(final Float newValue) {
				parent.setContactDamping(newValue);
			}
		};
	}
	public ContactDampingEditionMode(final GhostControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getContactDamping());
			}
			@Override
			public void set(final Float newValue) {
				parent.setContactDamping(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Contact damping");
	}
	
	@Override
	public Float get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final float cur = io.get();
		final float next = ((Float) value);
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

