package core.editables;

import com.jme3.bullet.collision.shapes.ConeCollisionShape;

public final class ConeCollisionShapeEditable extends CollisionShapeEditable {
	private float radius;
	private float height;
	
	private ConeCollisionShapeEditable(final ConeCollisionShape shape) {
		super(shape);
		
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
	protected void updateShape() {
		final ConeCollisionShape shape = new ConeCollisionShape(radius, height);
		shape.setMargin(getMargin());
		setShape(shape);
	}
	public static ConeCollisionShapeEditable valueOf(final ConeCollisionShape shape) {
		return(new ConeCollisionShapeEditable(shape));
	}
}
