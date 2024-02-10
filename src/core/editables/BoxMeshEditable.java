package core.editables;

import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

public final class BoxMeshEditable extends MeshEditable {
	private final Vector3f ext;
	
	public BoxMeshEditable(final Box box) {
		super(box);
		this.ext = new Vector3f(box.getXExtent(), box.getYExtent(), box.getZExtent());
		addEditionMode(new ExtendsEditionMode(this));
	}
	
	@Override
	public String toString() {
		return("Box mesh");
	}
	
	public static MeshEditable valueOf(final Box box) {
		return(new BoxMeshEditable(box));
	}
	
	public Vector3f getExtends() {
		final Box cast = (Box) getMesh();
		return(new Vector3f(cast.getXExtent(), cast.getYExtent(), cast.getZExtent()));
	}
	
	public void setExtends(final Vector3f newVec) {
		ext.set(new Vector3f(newVec));
		updateMesh();
	}
	
	private void updateMesh() {
		setMesh(new Box(ext.getX(), ext.getY(), ext.getZ()));
	}
}
