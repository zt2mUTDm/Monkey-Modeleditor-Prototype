package core.editables;

import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import com.simsilica.mathd.Quatd;
import com.simsilica.mathd.Vec3d;

import core.CollisionGroup;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class GhostControlEditable extends PhysicsControlEditable {
	private GhostControlEditable(final GhostControl control, final CollisionShapeEditable shape) {
		super(copyControl(control), shape);
		
		shape.addShapeChangedListener(new DataSink<CollisionShapeEditable>() {
			@Override
			public void sink(final CollisionShapeEditable value) {
				getControl().setCollisionShape(shape.getShape());
			}
		});
		
		addMode(new ApplyPhysicsLocalEditionMode(this));
		addMode(new ApplyScaleEditionMode(this));
		addMode(new CcdMotionThresholdEditionMode(this));
		addMode(new CcdSweptSphereRadiusEditionMode(this));
		for(int i = 0; i < 16; i++) {
			addMode(new CollideWithGroupEditionMode(i, this));
		}
		addMode(new CollisionGroupEditionMode(this));
		addMode(new ContactDampingEditionMode(this));
		addMode(new ContactProcessingThresholdEditionMode(this));
		addMode(new ContactStiffnessEditionMode(this));
		addMode(new DeactivationTimeEditionMode(this));
		addMode(new FrictionEditionMode(this));
		addMode(new PhysicsLocationEditionMode(this));
		addMode(new PhysicsLocationDpEditionMode(this));
		addMode(new PhysicsRotationEditionMode(this));
		addMode(new PhysicsRotationDpEditionMode(this));
		addMode(new RestitutionEditionMode(this));
		addMode(new RollingFrictionEditionMode(this));
		addMode(new SpinningFrictionEditionMode(this));
	}
	
	public void setApplyPhysicsLocal(final boolean newValue) {
		getControl().setApplyPhysicsLocal(newValue);
	}
	
	public boolean isApplyPhysicsLocal() {
		return(getControl().isApplyPhysicsLocal());
	}
	
	public void setApplyScale(final boolean newValue) {
		getControl().setApplyScale(newValue);
	}
	
	public boolean isApplyScale() {
		return(getControl().isApplyScale());
	}
	
	public void setCcdMotionThreshold(final float newValue) {
		getControl().setCcdMotionThreshold(newValue);
	}
	
	public float getCcdMotionThreshold() {
		return(getControl().getCcdMotionThreshold());
	}
	
	public void setCcdSweptSphereRadius(final float newValue) {
		getControl().setCcdSweptSphereRadius(newValue);
	}
	
	public float getCcdSweptSphereRadius() {
		return(getControl().getCcdSweptSphereRadius());
	}
	
	public void setCollideWithGroup(final int i, final boolean newValue) {
		final int mask = 1 << i;
		final int cur = getControl().getCollideWithGroups();
		if(newValue) {
			getControl().setCollideWithGroups(cur | mask);
		} else {
			getControl().setCollideWithGroups(cur & (~mask));
		}
	}
	
	public boolean getCollideWithGroup(final int i) {
		final int mask = 1 << i;
		final int cur = getControl().getCollideWithGroups();
		return((cur & mask) != 0);
	}
	
	public void setCollisionGroup(final CollisionGroup group) {
		getControl().setCollisionGroup(1 << group.ordinal());
	}
	
	public CollisionGroup getCollisionGroup() {
		final int log2Group = 31 - Integer.numberOfLeadingZeros(getControl().getCollisionGroup());
		return(CollisionGroup.values()[log2Group]);
	}
	
	public void setContactDamping(final float newValue) {
		getControl().setContactDamping(newValue);
	}
	
	public float getContactDamping() {
		return(getControl().getContactDamping());
	}
	
	public void setContactProcessingThreshold(final float newValue) {
		getControl().setContactProcessingThreshold(newValue);
	}
	
	public float getContactProcessingThreshold() {
		return(getControl().getContactProcessingThreshold());
	}
	
	public void setContactStiffness(final float newValue) {
		getControl().setContactStiffness(newValue);
	}
	
	public float getContactStiffness() {
		return(getControl().getContactStiffness());
	}
	
	public void setDeactivationTime(final float newValue) {
		getControl().setDeactivationTime(newValue);
	}
	
	public float getDeactivationTime() {
		return(getControl().getDeactivationTime());
	}
	
	public void setFriction(final float newValue) {
		getControl().setFriction(newValue);
	}
	
	public float getFriction() {
		return(getControl().getFriction());
	}
	
	public void setPhysicsLocation(final Vector3f newValue) {
		getControl().setPhysicsLocation(newValue);
	}
	
	public Vector3f getPhysicsLocation() {
		return(getControl().getPhysicsLocation(null));
	}
	
	public void setPhysicsLocationDp(final Vec3d newValue) {
		getControl().setPhysicsLocationDp(newValue);
	}
	
	public Vec3d getPhysicsLocationDp() {
		return(getControl().getPhysicsLocationDp(null));
	}
	
	public void setPhysicsRotation(final Quaternion newValue) {
		getControl().setPhysicsRotation(newValue);
	}
	
	public Quaternion getPhysicsRotation() {
		return(getControl().getPhysicsRotation(null));
	}
	
	public void setPhysicsRotationDp(final Quatd newValue) {
		getControl().setPhysicsRotationDp(newValue);
	}
	
	public Quatd getPhysicsRotationDp() {
		return(getControl().getPhysicsRotationDp(null));
	}
	
	public void setRestitution(final float newValue) {
		getControl().setRestitution(newValue);
	}
	
	public float getRestitution() {
		return(getControl().getRestitution());
	}
	
	public void setRollingFriction(final float newValue) {
		getControl().setRollingFriction(newValue);
	}
	
	public float getRollingFriction() {
		return(getControl().getRollingFriction());
	}
	
	public void setSpinningFriction(final float newValue) {
		getControl().setSpinningFriction(newValue);
	}
	
	public float getSpinningFriction() {
		return(getControl().getSpinningFriction());
	}
	
	@Override
	public GhostControl getControl() {
		return (GhostControl) (super.getControl());
	}
	
	private static GhostControl copyControl(final GhostControl control) {
		return(new Cloner().clone(control));
	}
	
	@Override
	public GhostControl createControl() {
		final GhostControl control = copyControl(getControl());
		control.setCollisionShape((ConvexShape) getShape().createShape());
		return(control);
	}
	
	public static GhostControlEditable valuesOf(final GhostControl control) {
		final CollisionShapeEditable shape = CollisionShapeEditable.valueOf(Requires.notNull(control, "control == null").getCollisionShape());
		return(new GhostControlEditable(control, shape));
	}
}
