package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class CollideWithGroupEditionMode implements EditionMode {
	private final int index;
	private final ValueIo<Boolean> io;
	
	public CollideWithGroupEditionMode(final int index, final VehicleControlEditable parent) {
		Requires.positive(index, "index < 0");
		this.index = Requires.lessThan(index, 32, "index >= 32");
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Boolean>() {
			@Override
			public Boolean get() {
				return(parent.getCollideWithGroup(index));
			}
			@Override
			public void set(final Boolean newValue) {
				parent.setCollideWithGroup(index, newValue);
			}
		};
	}
	public CollideWithGroupEditionMode(final int index, final RigidBodyControlEditable parent) {
		Requires.positive(index, "index < 0");
		this.index = Requires.lessThan(index, 32, "index >= 32");
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Boolean>() {
			@Override
			public Boolean get() {
				return(parent.getCollideWithGroup(index));
			}
			@Override
			public void set(final Boolean newValue) {
				parent.setCollideWithGroup(index, newValue);
			}
		};
	}
	public CollideWithGroupEditionMode(final int index, final CharacterControlEditable parent) {
		Requires.positive(index, "index < 0");
		this.index = Requires.lessThan(index, 32, "index >= 32");
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Boolean>() {
			@Override
			public Boolean get() {
				return(parent.getCollideWithGroup(index));
			}
			@Override
			public void set(final Boolean newValue) {
				parent.setCollideWithGroup(index, newValue);
			}
		};
	}
	public CollideWithGroupEditionMode(final int index, final GhostControlEditable parent) {
		Requires.positive(index, "index < 0");
		this.index = Requires.lessThan(index, 32, "index >= 32");
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Boolean>() {
			@Override
			public Boolean get() {
				return(parent.getCollideWithGroup(index));
			}
			@Override
			public void set(final Boolean newValue) {
				parent.setCollideWithGroup(index, newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Collide with group " + index);
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

