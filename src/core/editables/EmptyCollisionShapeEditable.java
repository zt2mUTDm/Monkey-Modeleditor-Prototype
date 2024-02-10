package core.editables;

import com.jme3.bullet.collision.shapes.EmptyShape;

import online.money_daisuki.api.base.Requires;

public final class EmptyCollisionShapeEditable extends CollisionShapeEditable {
	private EmptyCollisionShapeEditable(final EmptyShape shape) {
		super(Requires.notNull(shape, "shape == null"));
	}
	@Override
	protected void updateShape() {
		
	}
	public static EmptyCollisionShapeEditable valueOf(final EmptyShape shape) {
		return(new EmptyCollisionShapeEditable(shape));
	}
}
