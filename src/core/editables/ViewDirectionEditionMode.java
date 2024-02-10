package core.editables;

import com.jme3.math.Vector3f;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class ViewDirectionEditionMode implements EditionMode {
	private final ValueIo<Vector3f> io;
	
	public ViewDirectionEditionMode(final CharacterControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Vector3f>() {
			@Override
			public Vector3f get() {
				return(parent.getViewDirection());
			}
			@Override
			public void set(final Vector3f newValue) {
				parent.setViewDirection(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("View direction");
	}
	
	@Override
	public Vector3f get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Vector3f cur = get();
		final Vector3f next = ((Vector3f) value);
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
