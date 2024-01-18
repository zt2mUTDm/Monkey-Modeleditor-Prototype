package core.editables;

import com.jme3.scene.shape.Dome;

public final class DomeMeshEditable extends MeshEditable {
	private int planes;
	private int radialSamples;
	private float radius;
	
	public DomeMeshEditable(final Dome mesh) {
		super(mesh);
		
		planes = mesh.getPlanes();
		radialSamples = mesh.getRadialSamples();
		radius = mesh.getRadius();
		
		addEditionMode(new PlanesEditionMode(this));
		addEditionMode(new RadialSamplesEditionMode(this));
		addEditionMode(new RadiusEditionMode(this));
	}
	
	public int getPlanes() {
		return (planes);
	}
	public void setPlanes(final int planes) {
		this.planes = planes;
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
		return("Dome");
	}
	
	
	private void updateMesh() {
		setMesh(new Dome(planes, radialSamples, radius));
	}
	
	public static MeshEditable valueOf(final Dome mesh) {
		return(new DomeMeshEditable(mesh));
	}
}
