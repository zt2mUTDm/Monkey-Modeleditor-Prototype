package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class CircleSamplesEditionMode implements EditionMode {
	private final ValueIo<Integer> io;
	
	public CircleSamplesEditionMode(final TorusMeshEditable parent) {
		io = new ValueIo<Integer>() {
			@Override
			public Integer get() {
				return(parent.getZSamples());
			}
			@Override
			public void set(final Integer newValue) {
				parent.setZSamples(newValue.intValue());
			}
		};
	}

	@Override
	public String getName() {
		return("Z samples");
	}
	
	@Override
	public Integer get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Integer curName = io.get();
		final Integer newName = ((Integer) value);
		return(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				io.set(newName);
			}
		}, new Runnable() {
			@Override
			public void run() {
				io.set(curName);
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
