package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class InnerRadiusEditionMode implements EditionMode {
	private final ValueIo<Float> io;
	
	public InnerRadiusEditionMode(final TorusMeshEditable parent) {
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getInnerRadius());
			}
			@Override
			public void set(final Float newValue) {
				parent.setInnerRadius(newValue.floatValue());
			}
		};
	}
	
	@Override
	public String getName() {
		return("Inner radius");
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
	public EditionState createEditionState(EditionStateModel model) {
		throw new UnsupportedOperationException();
	}
}
