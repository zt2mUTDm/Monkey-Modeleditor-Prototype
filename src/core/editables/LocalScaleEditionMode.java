package core.editables;

import com.jme3.math.Vector3f;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import core.threed.TranslationState;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class LocalScaleEditionMode implements EditionMode {
	private final SpatialEditable spatial;
	
	public LocalScaleEditionMode(final SpatialEditable spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
	}
	
	@Override
	public String getName() {
		return("Local scale");
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Vector3f curName = spatial.getLocalScale();
		final Vector3f newName = ((Vector3f) value);
		return(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				spatial.setLocalScale(newName);
			}
		}, new Runnable() {
			@Override
			public void run() {
				spatial.setLocalScale(curName);
			}
		}));
	}
	
	@Override
	public Vector3f get() {
		return(spatial.getLocalScale());
	}
	
	@Override
	public boolean isEditableByTable() {
		return(true);
	}
	
	@Override
	public boolean isEditableByThreeDView() {
		return(true);
	}

	
	@Override
	public EditionState createEditionState(final EditionStateModel model) {
		return(new TranslationState(model, this, new WorldTranslationEditionMode(spatial)));
	}
}
