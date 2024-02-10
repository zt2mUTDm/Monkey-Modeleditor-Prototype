package core.editables;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;

import online.money_daisuki.api.base.Requires;

public final class CapsuleCollisionShapeEditable extends CollisionShapeEditable {
	private float radius;
	private float height;
	
	private CapsuleCollisionShapeEditable(final CapsuleCollisionShape shape) {
		super(Requires.notNull(shape, "shape == null"));
		
		radius = shape.getRadius();
		height = shape.getHeight();
		
		addEditionMode(new RadiusEditionMode(this));
		addEditionMode(new HeightEditionMode(this));
	}
	
	public float getHeight() {
		return (height);
	}
	public void setHeight(final float height) {
		this.height = height;
		updateShape();
	}
	
	public float getRadius() {
		return (radius);
	}
	public void setRadius(final float radius) {
		this.radius = radius;
		updateShape();
	}
	
	
	@Override
	public float getMargin() {
		return(0.0f);
	}
	@Override
	protected void updateShape() {
		final CapsuleCollisionShape shape = new CapsuleCollisionShape(radius, height);
		setShape(shape);
	}
	public static CapsuleCollisionShapeEditable valueOf(final CapsuleCollisionShape shape) {
		return(new CapsuleCollisionShapeEditable(shape));
	}
}
