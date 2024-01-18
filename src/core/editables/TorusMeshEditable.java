package core.editables;

import com.jme3.scene.shape.Torus;

public final class TorusMeshEditable extends MeshEditable {
	private int circleSamples;
	private int radialSamples;
	private float innerRadius;
	private float outerRadius;
	
	public TorusMeshEditable(final Torus mesh) {
		super(mesh);
		
		circleSamples = mesh.getCircleSamples();
		radialSamples = mesh.getRadialSamples();
		innerRadius = mesh.getInnerRadius();
		outerRadius = mesh.getOuterRadius();
		
		addEditionMode(new CircleSamplesEditionMode(this));
		addEditionMode(new RadialSamplesEditionMode(this));
		addEditionMode(new InnerRadiusEditionMode(this));
		addEditionMode(new OuterRadiusEditionMode(this));
	}
	public int getZSamples() {
		return (circleSamples);
	}
	public void setZSamples(final int circleSamples) {
		this.circleSamples = circleSamples;
		updateMesh();
	}
	public int getRadialSamples() {
		return (radialSamples);
	}
	public void setRadialSamples(final int radialSamples) {
		this.radialSamples = radialSamples;
		updateMesh();
	}
	public float getInnerRadius() {
		return (innerRadius);
	}
	public void setInnerRadius(final float innerRadius) {
		this.innerRadius = innerRadius;
		updateMesh();
	}
	public float getOuterRadius() {
		return (outerRadius);
	}
	public void setOuterRadius(final float outerRadius) {
		this.outerRadius = outerRadius;
		updateMesh();
	}
	
	@Override
	public String toString() {
		return("Torus");
	}
	
	private void updateMesh() {
		setMesh(new Torus(circleSamples, radialSamples, innerRadius, outerRadius));
	}
	
	public static MeshEditable valueOf(final Torus mesh) {
		return(new TorusMeshEditable(mesh));
	}
}
