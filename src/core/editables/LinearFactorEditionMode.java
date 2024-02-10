package core.editables;

import com.jme3.math.Vector3f;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class LinearFactorEditionMode implements EditionMode {
	private final ValueIo<Vector3f> io;
	
	public LinearFactorEditionMode(final VehicleControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Vector3f>() {
			@Override
			public Vector3f get() {
				return(parent.getLinearFactor());
			}
			@Override
			public void set(final Vector3f newValue) {
				parent.setLinearFactor(newValue);
			}
		};
	}
	public LinearFactorEditionMode(final RigidBodyControlEditable parent) {
		Requires.notNull(parent, "parent == null");
		io = new ValueIo<Vector3f>() {
			@Override
			public Vector3f get() {
				return(parent.getLinearFactor());
			}
			@Override
			public void set(final Vector3f newValue) {
				parent.setLinearFactor(newValue);
			}
		};
	}
	
	@Override
	public String getName() {
		return("Linear factor");
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
