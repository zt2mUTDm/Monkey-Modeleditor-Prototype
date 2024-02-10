package core.editables;

import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;

import online.money_daisuki.api.base.Requires;

public final class PlaneCollisionShapeEditable extends CollisionShapeEditable {
	private Vector3f normal;
	private float constant;
	
	private PlaneCollisionShapeEditable(final PlaneCollisionShape shape) {
		super(Requires.notNull(shape, "shape == null"));
		
		final Plane plane = shape.getPlane();
		normal = new Vector3f(plane.getNormal());
		constant = plane.getConstant();
		
		addEditionMode(new NormalEditionMode(this));
		addEditionMode(new ConstantEditionMode(this));
	}
	
	public void setNormal(final Vector3f normal) {
		this.normal = new Vector3f(normal);
		updateShape();
	}
	public Vector3f getNormal() {
		return(normal);
	}
	
	public void setConstant(final float constant) {
		this.constant = constant;
		updateShape();
	}
	public float getConstant() {
		return(constant);
	}
	
	@Override
	protected void updateShape() {
		final Plane plane = new Plane(normal, constant);
		final PlaneCollisionShape shape = new PlaneCollisionShape(plane);
		shape.setMargin(getMargin());
		setShape(shape);
	}
	public static PlaneCollisionShapeEditable valueOf(final PlaneCollisionShape shape) {
		return(new PlaneCollisionShapeEditable(shape));
	}
}
