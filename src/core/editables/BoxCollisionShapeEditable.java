package core.editables;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.Vector3f;

public final class BoxCollisionShapeEditable extends CollisionShapeEditable {
	private final Vector3f ext;
	
	private BoxCollisionShapeEditable(final BoxCollisionShape shape) {
		super(shape);
		
		ext = shape.getHalfExtents(null);
		
		addEditionMode(new ExtendsEditionMode(this));
	}
	
	public Vector3f getExtends() {
		final BoxCollisionShape cast = (BoxCollisionShape) getShape();
		return(cast.getHalfExtents(null));
	}
	
	public void setExtends(final Vector3f newVec) {
		ext.set(new Vector3f(newVec));
		updateShape();
	}
	
	@Override
	protected void updateShape() {
		setShape(new BoxCollisionShape(ext.getX(), ext.getY(), ext.getZ()));
	}
	
	public static BoxCollisionShapeEditable valueOf(final BoxCollisionShape shape) {
		return(new BoxCollisionShapeEditable(shape));
	}
}
