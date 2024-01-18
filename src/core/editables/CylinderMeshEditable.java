package core.editables;

import com.jme3.scene.shape.Cylinder;

public final class CylinderMeshEditable extends MeshEditable {
	private int axisSamples;
	private int radialSamples;
	private float radius;
	private float height;
	private boolean closed;
	private boolean inverted;
	
	public CylinderMeshEditable(final Cylinder cylinder) {
		super(cylinder);
		
		axisSamples = cylinder.getAxisSamples();
		radialSamples = cylinder.getRadialSamples();
		radius = cylinder.getRadius();
		height = cylinder.getHeight();
		closed = cylinder.isClosed();
		inverted = cylinder.isInverted();
		
		addEditionMode(new AxisSamplesEditionMode(this));
		addEditionMode(new RadialSamplesEditionMode(this));
		addEditionMode(new RadiusEditionMode(this));
		addEditionMode(new HeightEditionMode(this));
	}
	
	public int getAxisSamples() {
		return (axisSamples);
	}
	public void setAxisSamples(final int axisSamples) {
		this.axisSamples = axisSamples;
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
	public float getHeight() {
		return (height);
	}
	public void setHeight(final float height) {
		this.height = height;
		updateMesh();
	}
	public boolean isClosed() {
		return (closed);
	}
	public void setClosed(final boolean closed) {
		this.closed = closed;
		updateMesh();
	}
	public boolean isInverted() {
		return (inverted);
	}
	public void setInverted(final boolean inverted) {
		this.inverted = inverted;
		updateMesh();
	}
	
	@Override
	public String toString() {
		return("Cylinder mesh");
	}
	
	private void updateMesh() {
		setMesh(new Cylinder(axisSamples, radialSamples, radius, height, closed, inverted));
	}
	
	public static MeshEditable valueOf(final Cylinder cylinder) {
		return(new CylinderMeshEditable(cylinder));
	}
}
