package core.editables;

import com.jme3.bullet.collision.shapes.Box2dShape;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public final class Box2dCollisionShapeEditable extends CollisionShapeEditable {
	private final Vector2f ext;
	private final float margin;
	
	private Box2dCollisionShapeEditable(final Box2dShape shape) {
		super(shape);
		
		final Vector3f ext3d = shape.getHalfExtents(null);
		ext = new Vector2f(ext3d.x, ext3d.y);
		margin = shape.getMargin();
		
		addEditionMode(new Extends2dEditionMode(this));
	}
	
	public Vector2f getExtends() {
		final Box2dShape cast = (Box2dShape) getShape();
		final Vector3f vec = cast.getHalfExtents(null);
		return(new Vector2f(vec.x, vec.y));
	}
	
	public void setExtends(final Vector2f newVec) {
		ext.set(newVec);
		updateShape();
	}
	
	@Override
	protected void updateShape() {
		setShape(new Box2dShape(new Vector3f(ext.getX(), ext.getY(), margin)));
	}
	
	public static Box2dCollisionShapeEditable valueOf(final Box2dShape shape) {
		return(new Box2dCollisionShapeEditable(shape));
	}
}
