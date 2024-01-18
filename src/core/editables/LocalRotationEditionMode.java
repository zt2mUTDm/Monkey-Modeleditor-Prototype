package core.editables;

import com.jme3.math.Quaternion;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import core.threed.TranslationState;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class LocalRotationEditionMode implements EditionMode {
	private final SpatialEditable spatial;
	
	public LocalRotationEditionMode(final SpatialEditable spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
	}
	
	@Override
	public String getName() {
		return("Local rotation");
	}
	
	@Override
	public Quaternion get() {
		return(spatial.getLocalRotation());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Quaternion curName = spatial.getLocalRotation();
		final Quaternion newName = ((Quaternion) value);
		return(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				spatial.setLocalRotation(newName);
			}
		}, new Runnable() {
			@Override
			public void run() {
				spatial.setLocalRotation(curName);
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
		return(new TranslationState(model, this, new WorldTranslationEditionMode(spatial)));
	}
}
