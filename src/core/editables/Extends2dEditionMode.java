package core.editables;

import com.jme3.math.Vector2f;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class Extends2dEditionMode implements EditionMode {
	private final ValueIo<Vector2f> io;
	
	public Extends2dEditionMode(final Box2dCollisionShapeEditable box2dCollisionShapeEditable) {
		Requires.notNull(box2dCollisionShapeEditable, "parent == null");
		io = new ValueIo<Vector2f>() {
			@Override
			public Vector2f get() {
				return(box2dCollisionShapeEditable.getExtends());
			}
			@Override
			public void set(final Vector2f newValue) {
				box2dCollisionShapeEditable.setExtends(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Extends");
	}
	
	@Override
	public Vector2f get() {
		return(io.get());
	}
	
	@Override
	public Mapping<Runnable, Runnable> createChangeCommand(final Editable source, final Object value) {
		final Vector2f cur = get();
		final Vector2f next = ((Vector2f) value);
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
