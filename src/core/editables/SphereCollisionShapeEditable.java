package core.editables;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;

import online.money_daisuki.api.base.Requires;

public final class SphereCollisionShapeEditable extends CollisionShapeEditable {
	private float radius;
	
	protected SphereCollisionShapeEditable(final SphereCollisionShape shape) {
		super(Requires.notNull(shape, "shape == null"));
		
		radius = shape.getRadius();
		
		addEditionMode(new RadiusEditionMode(this));
	}
	
	@Override
	public float getMargin() {
		return(0);
	}
	
	public float getRadius() {
		return (radius);
	}
	public void setRadius(final float radius) {
		this.radius = radius;
		updateShape();
	}
	
	@Override
	protected void updateShape() {
		final SphereCollisionShape shape = new SphereCollisionShape(radius);
		setShape(shape);
	}
	
	public static SphereCollisionShapeEditable valueOf(final SphereCollisionShape shape) {
		return(new SphereCollisionShapeEditable(shape));
	}
}
