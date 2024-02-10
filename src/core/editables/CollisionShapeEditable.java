package core.editables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.jme3.bullet.collision.shapes.Box2dShape;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.ConeCollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.EmptyShape;
import com.jme3.bullet.collision.shapes.MultiSphere;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public abstract class CollisionShapeEditable implements Editable {
	private CollisionShape shape;
	
	private float margin;
	
	private final Collection<DataSink<? super CollisionShapeEditable>> shapeChangedListeners;
	private final List<EditionMode> modes;
	
	public CollisionShapeEditable(final CollisionShape shape) {
		this.shape = Requires.notNull(shape, "shape == null");
		this.shapeChangedListeners = new LinkedList<>();
		this.modes = new ArrayList<>();
		this.margin = shape.getMargin();
		
		addEditionMode(new MarginEditionMode(this));
	}
	protected void addEditionMode(final EditionMode mode) {
		this.modes.add(Requires.notNull(mode, "mode == null"));
	}
	
	public void setMargin(final float margin) {
		this.margin = margin;
		updateShape();
	}
	
	public float getMargin() {
		return(margin);
	}
	
	@Override
	public void setSelected(final boolean b) {
		
	}
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(new ArrayList<>(modes));
	}
	
	public CollisionShape getShape() {
		return(shape);
	}
	
	@Override
	public String toString() {
		return(shape.getClass().getSimpleName());
	}
	
	public void setShape(final CollisionShape shape) {
		this.shape = Requires.notNull(shape, "shape == null");
		fireShapeChangedListener();
	}
	
	protected CollisionShape createShape() {
		return(shape);
	}
	
	public void addShapeChangedListener(final DataSink<? super CollisionShapeEditable> l) {
		shapeChangedListeners.add(Requires.notNull(l, "l == null"));
	}
	public void removeShapeChangedListener(final DataSink<? super CollisionShapeEditable> l) {
		shapeChangedListeners.remove(Requires.notNull(l, "l == null"));
	}
	private void fireShapeChangedListener() {
		for(final DataSink<? super CollisionShapeEditable> l:shapeChangedListeners) {
			l.sink(this);
		}
	}
	
	protected abstract void updateShape();
	
	public static CollisionShapeEditable valueOf(final CollisionShape shape) {
		if(shape instanceof BoxCollisionShape) {
			return(BoxCollisionShapeEditable.valueOf((BoxCollisionShape)shape));
		} else if(shape instanceof Box2dShape) {
			return(Box2dCollisionShapeEditable.valueOf((Box2dShape)shape));
		} else if(shape instanceof CapsuleCollisionShape) {
			return(CapsuleCollisionShapeEditable.valueOf((CapsuleCollisionShape)shape));
		} else if(shape instanceof ConeCollisionShape) {
			return(ConeCollisionShapeEditable.valueOf((ConeCollisionShape)shape));
		} else if(shape instanceof CylinderCollisionShape) {
			return(CylinderCollisionShapeEditable.valueOf((CylinderCollisionShape)shape));
		} else if(shape instanceof EmptyShape) {
			return(EmptyCollisionShapeEditable.valueOf((EmptyShape)shape));
		} else if(shape instanceof MultiSphere) {
			return(MultiSphereCollisionShapeEditable.valueOf((MultiSphere)shape));
		} else if(shape instanceof PlaneCollisionShape) {
			return(PlaneCollisionShapeEditable.valueOf((PlaneCollisionShape)shape));
		} else if(shape instanceof SphereCollisionShape) {
			return(SphereCollisionShapeEditable.valueOf((SphereCollisionShape)shape));
		}
		throw new UnsupportedOperationException("Unknown shape type");
	}
}
