package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class NameEditionMode implements EditionMode {
	private final SpatialEditable spatial;
	
	public NameEditionMode(final SpatialEditable spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
	}
	
	@Override
	public String getName() {
		return("Name");
	}
	
	@Override
	public String get() {
		return(spatial.getName());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final String curName = spatial.getName();
		final String newName = ((String) value);
		return(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				spatial.setName(newName);
			}
		}, new Runnable() {
			@Override
			public void run() {
				spatial.setName(curName);
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
