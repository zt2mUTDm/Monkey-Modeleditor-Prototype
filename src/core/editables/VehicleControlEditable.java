package core.editables;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import com.simsilica.mathd.Quatd;
import com.simsilica.mathd.Vec3d;

import core.CollisionGroup;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class VehicleControlEditable extends PhysicsControlEditable {
	private final VehicleControl storageControl;
	
	private VehicleControlEditable(final VehicleControl control, final CollisionShapeEditable shape) {
		super(new RigidBodyControl(shape.getShape(), 0.0f), Requires.notNull(shape, "shape == null"));
		
		shape.addShapeChangedListener(new DataSink<CollisionShapeEditable>() {
			@Override
			public void sink(final CollisionShapeEditable value) {
				getControl().setCollisionShape(shape.getShape());
			}
		});
		
		
		storageControl = copyControl(control);
		
		final RigidBodyControl c = getControl();
		c.setMass(1.0f);
		c.setKinematic(true);
		
		addMode(new AngularDampingEditionMode(this));
		addMode(new AngularFactorEditionMode(this));
		addMode(new AngularSleepingThresholdEditionMode(this));
		addMode(new AngularFactorEditionMode(this));
		addMode(new AngularVelocityEditionMode(this));
		addMode(new AngularVelocityDpEditionMode(this));
		addMode(new ApplyPhysicsLocalEditionMode(this));
		addMode(new CcdMotionThresholdEditionMode(this));
		addMode(new CcdSweptSphereRadiusEditionMode(this));
		for(int i = 0; i < 16; i++) {
			addMode(new CollideWithGroupEditionMode(i, this));
		}
		addMode(new CollisionGroupEditionMode(this));
		addMode(new ContactDampingEditionMode(this));
		addMode(new ContactProcessingThresholdEditionMode(this));
		addMode(new ContactResponseEditionMode(this));
		addMode(new ContactStiffnessEditionMode(this));
		addMode(new DeactivationTimeEditionMode(this));
		addMode(new FrictionEditionMode(this));
		addMode(new FrictionSlipEditionMode(this));
		addMode(new InverseInertiaLocalEditionMode(this));
		addMode(new KinematicEditionMode(this));
		addMode(new LinearDampingEditionMode(this));
		addMode(new LinearFactorEditionMode(this));
		addMode(new LinearSleepingThresholdEditionMode(this));
		addMode(new LinearVelocityEditionMode(this));
		addMode(new LinearVelocityDpEditionMode(this));
		addMode(new MassEditionMode(this));
		addMode(new MaxSuspensionForceEditionMode(this));
		addMode(new MaxSuspensionTravelCmEditionMode(this));
		addMode(new PhysicsLocationEditionMode(this));
		addMode(new PhysicsLocationDpEditionMode(this));
		addMode(new PhysicsRotationEditionMode(this));
		addMode(new PhysicsRotationDpEditionMode(this));
		addMode(new PhysicsScaleEditionMode(this));
		addMode(new RestitutionEditionMode(this));
		addMode(new RollingFrictionEditionMode(this));
		addMode(new SpinningFrictionEditionMode(this));
		addMode(new SuspensionCompressionEditionMode(this));
		addMode(new SuspensionDampingEditionMode(this));
		addMode(new SuspensionStiffnessEditionMode(this));
	}
	
	public void setAngularDamping(final float newValue) {
		storageControl.setAngularDamping(newValue);
	}
	
	public float getAngularDamping() {
		return(storageControl.getAngularDamping());
	}
	
	public void setAngularFactor(final Vector3f newValue) {
		storageControl.setAngularFactor(newValue);
	}
	
	public Vector3f getAngularFactor() {
		return(storageControl.getAngularFactor(null));
	}
	
	public void setAngularSleepingThreshold(final float newValue) {
		storageControl.setAngularSleepingThreshold(newValue);
	}
	
	public float getAngularSleepingThreshold() {
		return(storageControl.getAngularSleepingThreshold());
	}
	
	public void setAngularVelocity(final Vector3f newValue) {
		storageControl.setAngularFactor(newValue);
	}
	
	public Vector3f getAngularVelocity() {
		return(storageControl.getAngularVelocity(null));
	}
	
	public void setAngularVelocityDp(final Vec3d newValue) {
		storageControl.setAngularVelocityDp(newValue);
	}
	
	public Vec3d getAngularVelocityDp() {
		return(storageControl.getAngularVelocityDp(null));
	}
	
	public void setApplyPhysicsLocal(final boolean newValue) {
		storageControl.setApplyPhysicsLocal(newValue);
	}
	
	public boolean isApplyPhysicsLocal() {
		return(storageControl.isApplyPhysicsLocal());
	}
	
	public void setCcdMotionThreshold(final float newValue) {
		storageControl.setCcdMotionThreshold(newValue);
	}
	
	public float getCcdMotionThreshold() {
		return(storageControl.getCcdMotionThreshold());
	}
	
	public void setCcdSweptSphereRadius(final float newValue) {
		storageControl.setCcdSweptSphereRadius(newValue);
	}
	
	public float getCcdSweptSphereRadius() {
		return(storageControl.getCcdSweptSphereRadius());
	}
	
	public void setCollideWithGroup(final int i, final boolean newValue) {
		final int mask = 1 << i;
		final int cur = storageControl.getCollideWithGroups();
		if(newValue) {
			storageControl.setCollideWithGroups(cur | mask);
		} else {
			storageControl.setCollideWithGroups(cur & (~mask));
		}
	}
	
	public boolean getCollideWithGroup(final int i) {
		final int mask = 1 << i;
		final int cur = storageControl.getCollideWithGroups();
		return((cur & mask) != 0);
	}
	
	public void setCollisionGroup(final CollisionGroup group) {
		storageControl.setCollisionGroup(1 << group.ordinal());
	}
	
	public CollisionGroup getCollisionGroup() {
		final int log2Group = 31 - Integer.numberOfLeadingZeros(storageControl.getCollisionGroup());
		return(CollisionGroup.values()[log2Group]);
	}
	
	public void setContactDamping(final float newValue) {
		storageControl.setContactDamping(newValue);
	}
	
	public float getContactDamping() {
		return(storageControl.getContactDamping());
	}
	
	public void setContactProcessingThreshold(final float newValue) {
		storageControl.setContactProcessingThreshold(newValue);
	}
	
	public float getContactProcessingThreshold() {
		return(storageControl.getContactProcessingThreshold());
	}
	
	public void setContactResponse(final boolean newValue) {
		storageControl.setContactResponse(newValue);
	}
	
	public boolean isContactResponse() {
		return(storageControl.isContactResponse());
	}
	
	public void setContactStiffness(final float newValue) {
		storageControl.setContactStiffness(newValue);
	}
	
	public float getContactStiffness() {
		return(storageControl.getContactStiffness());
	}
	
	public void setDeactivationTime(final float newValue) {
		storageControl.setDeactivationTime(newValue);
	}
	
	public float getDeactivationTime() {
		return(storageControl.getDeactivationTime());
	}
	
	public void setFriction(final float newValue) {
		storageControl.setFriction(newValue);
	}
	
	public float getFriction() {
		return(storageControl.getFriction());
	}
	
	public void setFrictionSlip(final float newValue) {
		storageControl.setFrictionSlip(newValue);
	}
	
	public float getFrictionSlip() {
		return(storageControl.getFrictionSlip());
	}
	
	public void setInverseInertiaLocal(final Vector3f newValue) {
		storageControl.setInverseInertiaLocal(newValue);
	}
	
	public Vector3f getInverseInertiaLocal() {
		return(storageControl.getInverseInertiaLocal(null));
	}
	
	public void setKinematic(final boolean newValue) {
		storageControl.setKinematic(newValue);
	}
	
	public boolean isKinematic() {
		return(storageControl.isKinematic());
	}
	
	public void setLinearDamping(final float newValue) {
		storageControl.setLinearDamping(newValue);
	}
	
	public float getLinearDamping() {
		return(storageControl.getLinearDamping());
	}
	
	public void setLinearFactor(final Vector3f newValue) {
		storageControl.setLinearFactor(newValue);
	}
	
	public Vector3f getLinearFactor() {
		return(storageControl.getLinearFactor(null));
	}
	
	public void setLinearSleepingThreshold(final float newValue) {
		storageControl.setLinearSleepingThreshold(newValue);
	}
	
	public float getLinearSleepingThreshold() {
		return(storageControl.getLinearSleepingThreshold());
	}
	
	public void setLinearVelocity(final Vector3f newValue) {
		storageControl.setLinearVelocity(newValue);
	}
	
	public Vector3f getLinearVelocity() {
		return(storageControl.getLinearVelocity(null));
	}
	
	public void setLinearVelocityDp(final Vec3d newValue) {
		storageControl.setLinearVelocityDp(newValue);
	}
	
	public Vec3d getLinearVelocityDp() {
		return(storageControl.getLinearVelocityDp(null));
	}
	
	public void setMass(final float newValue) {
		storageControl.setMass(newValue);
	}
	
	public float getMass() {
		return(storageControl.getMass());
	}
	
	public void setMaxSuspensionForce(final float newValue) {
		storageControl.setMaxSuspensionForce(newValue);
	}
	
	public float getMaxSuspensionForce() {
		return(storageControl.getMaxSuspensionForce());
	}
	
	public void setMaxSuspensionTravelCm(final float newValue) {
		storageControl.setMaxSuspensionTravelCm(newValue);
	}
	
	public float getMaxSuspensionTravelCm() {
		return(storageControl.getMaxSuspensionTravelCm());
	}
	
	public void setPhysicsLocation(final Vector3f newValue) {
		storageControl.setPhysicsLocation(newValue);
	}
	
	public Vector3f getPhysicsLocation() {
		return(storageControl.getPhysicsLocation(null));
	}
	
	public void setPhysicsLocationDp(final Vec3d newValue) {
		storageControl.setPhysicsLocationDp(newValue);
	}
	
	public Vec3d getPhysicsLocationDp() {
		return(storageControl.getPhysicsLocationDp(null));
	}
	
	public void setPhysicsRotation(final Quaternion newValue) {
		storageControl.setPhysicsRotation(newValue);
	}
	
	public Quaternion getPhysicsRotation() {
		return(storageControl.getPhysicsRotation(null));
	}
	
	public void setPhysicsRotationDp(final Quatd newValue) {
		storageControl.setPhysicsRotationDp(newValue);
	}
	
	public Quatd getPhysicsRotationDp() {
		return(storageControl.getPhysicsRotationDp(null));
	}
	
	public void setPhysicsScale(final Vector3f newValue) {
		storageControl.setPhysicsScale(newValue);
	}
	
	public Vector3f getPhysicsScale() {
		return(storageControl.getScale(null));
	}
	
	public void setRestitution(final float newValue) {
		storageControl.setRestitution(newValue);
	}
	
	public float getRestitution() {
		return(storageControl.getRestitution());
	}
	
	public void setRollingFriction(final float newValue) {
		storageControl.setRollingFriction(newValue);
	}
	
	public float getRollingFriction() {
		return(storageControl.getRollingFriction());
	}
	
	public void setSpinningFriction(final float newValue) {
		storageControl.setSpinningFriction(newValue);
	}
	
	public float getSpinningFriction() {
		return(storageControl.getSpinningFriction());
	}
	
	public void setSuspensionCompression(final float newValue) {
		storageControl.setSuspensionCompression(newValue);
	}
	
	public float getSuspensionCompression() {
		return(storageControl.getSuspensionCompression());
	}
	
	public void setSuspensionDamping(final float newValue) {
		storageControl.setSuspensionDamping(newValue);
	}
	
	public float getSuspensionDamping() {
		return(storageControl.getSuspensionDamping());
	}
	
	public void setSuspensionStiffness(final float newValue) {
		storageControl.setSuspensionStiffness(newValue);
	}
	
	public float getSuspensionStiffness() {
		return(storageControl.getSuspensionStiffness());
	}
	
	@Override
	public RigidBodyControl getControl() {
		return (RigidBodyControl) (super.getControl());
	}
	
	@Override
	public VehicleControl createControl() {
		final VehicleControl control = copyControl(storageControl);
		control.setCollisionShape((ConvexShape) getShape().createShape());
		return(control);
	}
	
	private VehicleControl copyControl(final VehicleControl control) {
		return(new Cloner().clone(control));
	}
	
	@Override
	public String toString() {
		return(VehicleControl.class.getName());
	}
	
	public static VehicleControlEditable valuesOf(final VehicleControl control) {
		final CollisionShape s = Requires.notNull(control, "control == null").getCollisionShape();
		final CollisionShapeEditable shape = CollisionShapeEditable.valueOf(s);
		return(new VehicleControlEditable(control, shape));
	}
}
