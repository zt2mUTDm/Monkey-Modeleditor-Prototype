package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class RadiusEditionMode implements EditionMode {
	private final ValueIo<Float> io;
	
	public RadiusEditionMode(final CylinderMeshEditable parent) {
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getRadius());
			}
			@Override
			public void set(final Float newValue) {
				parent.setRadius(newValue.floatValue());
			}
		};
	}
	public RadiusEditionMode(final SphereMeshEditable parent) {
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getRadius());
			}
			@Override
			public void set(final Float newValue) {
				parent.setRadius(newValue.floatValue());
			}
		};
	}
	public RadiusEditionMode(final DomeMeshEditable parent) {
		io = new ValueIo<Float>() {
			@Override
			public Float get() {
				return(parent.getRadius());
			}
			@Override
			public void set(final Float newValue) {
				parent.setRadius(newValue.floatValue());
			}
		};
	}
	
	@Override
	public String getName() {
		return("Radius");
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
