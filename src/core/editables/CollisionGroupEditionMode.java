package core.editables;

import core.CollisionGroup;
import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class CollisionGroupEditionMode implements EditionMode {
	private final ValueIo<CollisionGroup> io;
	
	public CollisionGroupEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<CollisionGroup>() {
			@Override
			public CollisionGroup get() {
				return(parent.getCollisionGroup());
			}
			@Override
			public void set(final CollisionGroup newValue) {
				parent.setCollisionGroup(newValue);
			}
		};
	}
	public CollisionGroupEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<CollisionGroup>() {
			@Override
			public CollisionGroup get() {
				return(parent.getCollisionGroup());
			}
			@Override
			public void set(final CollisionGroup newValue) {
				parent.setCollisionGroup(newValue);
			}
		};
	}
	public CollisionGroupEditionMode(final CharacterControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<CollisionGroup>() {
			@Override
			public CollisionGroup get() {
				return(parent.getCollisionGroup());
			}
			@Override
			public void set(final CollisionGroup newValue) {
				parent.setCollisionGroup(newValue);
			}
		};
	}
	public CollisionGroupEditionMode(final GhostControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<CollisionGroup>() {
			@Override
			public CollisionGroup get() {
				return(parent.getCollisionGroup());
			}
			@Override
			public void set(final CollisionGroup newValue) {
				parent.setCollisionGroup(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Collision group");
	}
	
	@Override
	public CollisionGroup get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final CollisionGroup cur = io.get();
		final CollisionGroup next = ((CollisionGroup) value);
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

