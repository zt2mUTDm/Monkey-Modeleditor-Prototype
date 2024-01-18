package core.editables;

import com.jme3.math.Vector3f;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class ExtendsEditionMode implements EditionMode {
	private final BoxMeshEditable box;
	
	public ExtendsEditionMode(final BoxMeshEditable box) {
		this.box = Requires.notNull(box, "box == null");
	}
	
	@Override
	public String getName() {
		return("Extends");
	}
	
	@Override
	public Vector3f get() {
		return(box.getExtends());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Vector3f curName = box.getExtends();
		final Vector3f newName = ((Vector3f) value);
		return(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				box.setExtends(newName);
			}
		}, new Runnable() {
			@Override
			public void run() {
				box.setExtends(curName);
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
