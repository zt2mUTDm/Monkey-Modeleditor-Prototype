package core.editables;

import com.jme3.bullet.collision.shapes.MultiSphere;

import online.money_daisuki.api.base.Requires;

public final class MultiSphereCollisionShapeEditable extends CollisionShapeEditable {
	private float radius;
	private float height;
	
	private MultiSphereCollisionShapeEditable(final MultiSphere shape) {
		super(Requires.notNull(shape, "shape == null"));
		
		
	}
	@Override
	protected void updateShape() {
		
	}
	public static MultiSphereCollisionShapeEditable valueOf(final MultiSphere shape) {
		return(new MultiSphereCollisionShapeEditable(shape));
	}
}
