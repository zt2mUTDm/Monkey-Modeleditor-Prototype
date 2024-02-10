package core.editables;

import core.Axis;
import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class AxisOfHeightEditionMode implements EditionMode {
	private final ValueIo<Axis> io;
	
	public AxisOfHeightEditionMode(final CylinderCollisionShapeEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Axis>() {
			@Override
			public Axis get() {
				return(parent.getAxisOfHeight());
			}
			@Override
			public void set(final Axis newValue) {
				parent.setAxisOfHeight(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Axis of height");
	}
	
	@Override
	public Axis get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Axis cur = io.get();
		final Axis next = ((Axis) value);
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
