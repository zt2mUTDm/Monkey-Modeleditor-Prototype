package core.editables;

import com.jme3.scene.shape.Sphere;

public final class SphereMeshEditable extends MeshEditable {
	private int zSamples;
	private int radialSamples;
	private float radius;
	
	public SphereMeshEditable(final Sphere mesh) {
		super(mesh);
		
		zSamples = mesh.getZSamples();
		radialSamples = mesh.getRadialSamples();
		radius = mesh.getRadius();
		
		addEditionMode(new ZSamplesEditionMode(this));
		addEditionMode(new RadialSamplesEditionMode(this));
		addEditionMode(new RadiusEditionMode(this));
	}
	
	public int getZSamples() {
		return (zSamples);
	}
	public void setZSamples(final int zSamples) {
		this.zSamples = zSamples;
		updateMesh();
	}
	public int getRadialSamples() {
		return (radialSamples);
	}
	public void setRadialSamples(final int radialSamples) {
		this.radialSamples = radialSamples;
		updateMesh();
	}
	public float getRadius() {
		return (radius);
	}
	public void setRadius(final float radius) {
		this.radius = radius;
		updateMesh();
	}
	
	@Override
	public String toString() {
		return("Sphere mesh");
	}
	
	private void updateMesh() {
		setMesh(new Sphere(zSamples, radialSamples, radius));
	}
	
	public static MeshEditable valueOf(final Sphere mesh) {
		return(new SphereMeshEditable(mesh));
	}
}
