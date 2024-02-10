package core.editables;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import com.simsilica.mathd.Vec3d;

import core.CollisionGroup;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class CharacterControlEditable extends PhysicsControlEditable {
	private final CharacterControl storageControl;
	
	private CharacterControlEditable(final CharacterControl control, final CollisionShapeEditable shape) {
		super(new RigidBodyControl(shape.getShape(), 0.0f), Requires.notNull(shape, "shape == null"));
		
		shape.addShapeChangedListener(new DataSink<CollisionShapeEditable>() {
			@Override
			public void sink(final CollisionShapeEditable value) {
				getControl().setCollisionShape(shape.getShape());
			}
		});
		
		
		storageControl = copyControl(control);
		storageControl.setSpatial(null);
		
		final RigidBodyControl c = getControl();
		c.setMass(1.0f);
		c.setKinematic(true);
		
		addMode(new AngularDampingEditionMode(this));
		addMode(new AngularVelocityEditionMode(this));
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
		addMode(new FallSpeedEditionMode(this));
		addMode(new FrictionEditionMode(this));
		addMode(new JumpSpeedEditionMode(this));
		addMode(new LinearDampingEditionMode(this));
		addMode(new LinearVelocityEditionMode(this));
		addMode(new PhysicsLocationEditionMode(this));
		addMode(new PhysicsLocationDpEditionMode(this));
		addMode(new RestitutionEditionMode(this));
		addMode(new RollingFrictionEditionMode(this));
		addMode(new SpinningFrictionEditionMode(this));
		addMode(new StepHeightEditionMode(this));
		addMode(new SweepTestEditionMode(this));
		addMode(new UpEditionMode(this));
		addMode(new WalkDirectionEditionMode(this));
		addMode(new ViewDirectionEditionMode(this));
	}
	
	public void setAngularDamping(final float newValue) {
		storageControl.getCharacter().setAngularDamping(newValue);
	}
	
	public float getAngularDamping() {
		return(storageControl.getCharacter().getAngularDamping());
	}
	
	public void setAngularVelocity(final Vector3f newValue) {
		storageControl.getCharacter().setAngularVelocity(newValue);
	}
	
	public Vector3f getAngularVelocity() {
		return(storageControl.getCharacter().getAngularVelocity(null));
	}
	
	public void setApplyPhysicsLocal(final boolean newValue) {
		storageControl.setApplyPhysicsLocal(newValue);
	}
	
	public boolean isApplyPhysicsLocal() {
		return(storageControl.isApplyPhysicsLocal());
	}
	
	public void setCcdMotionThreshold(final float newValue) {
		storageControl.getCharacter().setCcdMotionThreshold(newValue);
	}
	
	public float getCcdMotionThreshold() {
		return(storageControl.getCharacter().getCcdMotionThreshold());
	}
	
	public void setCcdSweptSphereRadius(final float newValue) {
		storageControl.getCharacter().setCcdSweptSphereRadius(newValue);
	}
	
	public float getCcdSweptSphereRadius() {
		return(storageControl.getCharacter().getCcdSweptSphereRadius());
	}
	
	public void setCollideWithGroup(final int i, final boolean newValue) {
		final int mask = 1 << i;
		final int cur = storageControl.getCharacter().getCollideWithGroups();
		if(newValue) {
			storageControl.getCharacter().setCollideWithGroups(cur | mask);
		} else {
			storageControl.getCharacter().setCollideWithGroups(cur & (~mask));
		}
	}
	
	public boolean getCollideWithGroup(final int i) {
		final int mask = 1 << i;
		final int cur = storageControl.getCharacter().getCollideWithGroups();
		return((cur & mask) != 0);
	}
	
	public void setCollisionGroup(final CollisionGroup group) {
		storageControl.getCharacter().setCollisionGroup(1 << group.ordinal());
	}
	
	public CollisionGroup getCollisionGroup() {
		final int log2Group = 31 - Integer.numberOfLeadingZeros(storageControl.getCharacter().getCollisionGroup());
		return(CollisionGroup.values()[log2Group]);
	}
	
	public void setContactDamping(final float newValue) {
		storageControl.getCharacter().setContactDamping(newValue);
	}
	
	public float getContactDamping() {
		return(storageControl.getCharacter().getContactDamping());
	}
	
	public void setContactProcessingThreshold(final float newValue) {
		storageControl.getCharacter().setContactProcessingThreshold(newValue);
	}
	
	public float getContactProcessingThreshold() {
		return(storageControl.getCharacter().getContactProcessingThreshold());
	}
	
	public void setContactResponse(final boolean newValue) {
		storageControl.getCharacter().setContactResponse(newValue);
	}
	
	public boolean isContactResponse() {
		return(storageControl.getCharacter().isContactResponse());
	}
	
	public void setContactStiffness(final float newValue) {
		storageControl.getCharacter().setContactStiffness(newValue);
	}
	
	public float getContactStiffness() {
		return(storageControl.getCharacter().getContactStiffness());
	}
	
	public void setDeactivationTime(final float newValue) {
		storageControl.getCharacter().setDeactivationTime(newValue);
	}
	
	public float getDeactivationTime() {
		return(storageControl.getCharacter().getDeactivationTime());
	}
	
	public void setFallSpeed(final float newValue) {
		storageControl.getCharacter().setFallSpeed(newValue);
	}
	
	public float getFallSpeed() {
		return(storageControl.getCharacter().getFallSpeed());
	}
	
	public void setFriction(final float newValue) {
		storageControl.getCharacter().setFriction(newValue);
	}
	
	public float getFriction() {
		return(storageControl.getCharacter().getFriction());
	}
	
	public void setJumpSpeed(final float newValue) {
		storageControl.getCharacter().setJumpSpeed(newValue);
	}
	
	public float getJumpSpeed() {
		return(storageControl.getCharacter().getJumpSpeed());
	}
	
	public void setLinearDamping(final float newValue) {
		storageControl.getCharacter().setLinearDamping(newValue);
	}
	
	public float getLinearDamping() {
		return(storageControl.getCharacter().getLinearDamping());
	}
	
	public void setLinearVelocity(final Vector3f newValue) {
		storageControl.getCharacter().setLinearVelocity(newValue);
	}
	
	public Vector3f getLinearVelocity() {
		return(storageControl.getCharacter().getLinearVelocity(null));
	}
	
	public void setPhysicsLocation(final Vector3f newValue) {
		storageControl.getCharacter().setPhysicsLocation(newValue);
	}
	
	public Vector3f getPhysicsLocation() {
		return(storageControl.getCharacter().getPhysicsLocation(null));
	}
	
	public void setPhysicsLocationDp(final Vec3d newValue) {
		storageControl.getCharacter().setPhysicsLocationDp(newValue);
	}
	
	public Vec3d getPhysicsLocationDp() {
		return(storageControl.getCharacter().getPhysicsLocationDp(null));
	}
	
	public void setRestitution(final float newValue) {
		storageControl.getCharacter().setRestitution(newValue);
	}
	
	public float getRestitution() {
		return(storageControl.getCharacter().getRestitution());
	}
	
	public void setRollingFriction(final float newValue) {
		storageControl.getCharacter().setRollingFriction(newValue);
	}
	
	public float getRollingFriction() {
		return(storageControl.getCharacter().getRollingFriction());
	}
	
	public void setSpinningFriction(final float newValue) {
		storageControl.getCharacter().setSpinningFriction(newValue);
	}
	
	public float getSpinningFriction() {
		return(storageControl.getCharacter().getSpinningFriction());
	}
	
	public void setStepHeight(final float newValue) {
		storageControl.getCharacter().setStepHeight(newValue);
	}
	
	public float getStepHeight() {
		return(storageControl.getCharacter().getStepHeight());
	}
	
	public void setSweepTest(final boolean newValue) {
		storageControl.getCharacter().setSweepTest(newValue);
	}
	
	public boolean isSweepTest() {
		return(storageControl.getCharacter().isUsingGhostSweepTest());
	}
	
	public void setUp(final Vector3f newValue) {
		storageControl.getCharacter().setUp(newValue);
	}
	
	public Vector3f getUp() {
		return(storageControl.getCharacter().getUpDirection(null));
	}
	
	public void setWalkDirection(final Vector3f newValue) {
		storageControl.getCharacter().setWalkDirection(newValue);
	}
	
	public Vector3f getWalkDirection() {
		return(storageControl.getCharacter().getWalkDirection(null));
	}
	
	public void setViewDirection(final Vector3f newValue) {
		storageControl.setViewDirection(newValue);
	}
	
	public Vector3f getViewDirection() {
		return(storageControl.getViewDirection(null));
	}
	
	@Override
	public RigidBodyControl getControl() {
		return (RigidBodyControl) (super.getControl());
	}
	
	@Override
	public CharacterControl createControl() {
		return(copyControl(storageControl));
	}
	
	private CharacterControl copyControl(final CharacterControl control) {
		return(new Cloner().clone(control));
	}
	
	@Override
	public String toString() {
		return(CharacterControl.class.getName());
	}
	
	public static CharacterControlEditable valuesOf(final CharacterControl control) {
		final CollisionShape s = control.getCharacter().getCollisionShape();
		final CollisionShapeEditable shape = CollisionShapeEditable.valueOf(s);
		return(new CharacterControlEditable(control, shape));
	}
}
