package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class CcdMotionThresholdEditionMode implements EditionMode {
	private final ValueIo<Float> io;
	
	public CcdMotionThresholdEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getCcdMotionThreshold());
			}
			@Override
			public void set(final Float newValue) {
				parent.setCcdMotionThreshold(newValue);
			}
		};
	}
	public CcdMotionThresholdEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getCcdMotionThreshold());
			}
			@Override
			public void set(final Float newValue) {
				parent.setCcdMotionThreshold(newValue);
			}
		};
	}
	public CcdMotionThresholdEditionMode(final CharacterControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getCcdMotionThreshold());
			}
			@Override
			public void set(final Float newValue) {
				parent.setCcdMotionThreshold(newValue);
			}
		};
	}
	public CcdMotionThresholdEditionMode(final GhostControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getCcdMotionThreshold());
			}
			@Override
			public void set(final Float newValue) {
				parent.setCcdMotionThreshold(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("CCD motion threshold");
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

