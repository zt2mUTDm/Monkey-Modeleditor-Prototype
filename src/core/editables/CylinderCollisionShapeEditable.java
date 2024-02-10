package core.editables;

import com.jme3.bullet.collision.shapes.CylinderCollisionShape;

import core.Axis;
import online.money_daisuki.api.base.Requires;

public final class CylinderCollisionShapeEditable extends CollisionShapeEditable {
	private float radius;
	private float height;
	private Axis axisOfHeight;
	
	private CylinderCollisionShapeEditable(final CylinderCollisionShape shape) {
		super(Requires.notNull(shape, "shape == null"));
		
		radius = shape.maxRadius();
		height = shape.getHeight();
		axisOfHeight = Axis.values()[shape.getAxis()];
		
		addEditionMode(new RadiusEditionMode(this));
		addEditionMode(new HeightEditionMode(this));
		addEditionMode(new AxisOfHeightEditionMode(this));
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
	
	public Axis getAxisOfHeight() {
		return (axisOfHeight);
	}
	public void setAxisOfHeight(final Axis axis) {
		this.axisOfHeight = Requires.notNull(axis, "axis == null");
		updateShape();
	}
	
	@Override
	protected void updateShape() {
		final CylinderCollisionShape shape = new CylinderCollisionShape(radius, height, axisOfHeight.ordinal());
		shape.setMargin(getMargin());
		setShape(shape);
	}
	public static CylinderCollisionShapeEditable valueOf(final CylinderCollisionShape shape) {
		return(new CylinderCollisionShapeEditable(shape));
	}
}
