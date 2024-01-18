package core.editables;

import com.jme3.math.Vector3f;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class WorldTranslationEditionMode implements EditionMode {
	private final SpatialEditable spatial;
	
	public WorldTranslationEditionMode(final SpatialEditable spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
	}
	
	@Override
	public String getName() {
		return("World translation");
	}
	
	@Override
	public Vector3f get() {
		return(spatial.getWorldTranslation());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isEditableByTable() {
		return(false);
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
