package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class HeightEditionMode implements EditionMode {
	private final ValueIo<Float> io;
	
	public HeightEditionMode(final CylinderMeshEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getHeight());
			}
			@Override
			public void set(final Float newValue) {
				parent.setHeight(newValue.floatValue());
			}
		};
	}
	public HeightEditionMode(final CapsuleCollisionShapeEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getHeight());
			}
			@Override
			public void set(final Float newValue) {
				parent.setHeight(newValue.floatValue());
			}
		};
	}
	public HeightEditionMode(final ConeCollisionShapeEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getHeight());
			}
			@Override
			public void set(final Float newValue) {
				parent.setHeight(newValue.floatValue());
			}
		};
	}
	public HeightEditionMode(final CylinderCollisionShapeEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getHeight());
			}
			@Override
			public void set(final Float newValue) {
				parent.setHeight(newValue.floatValue());
			}
		};
	}
	
	@Override
	public String getName() {
		return("Height");
	}
	
	@Override
	public Float get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Float curValue = io.get();
		final Float newValue = ((Float) value);
		return(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				io.set(newValue);
			}
		}, new Runnable() {
			@Override
			public void run() {
				io.set(curValue);
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
