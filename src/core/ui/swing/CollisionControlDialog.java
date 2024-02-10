package core.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.jme3.bullet.collision.shapes.Box2dShape;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.ConeCollisionShape;
import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.EmptyShape;
import com.jme3.bullet.collision.shapes.MultiSphere;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.Plane;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;

import online.money_daisuki.api.gui.swing.layouts.InputLayout;

public final class CollisionControlDialog {
	private final JDialog dialog;
	
	private final JComboBox<ControlType> controlTypeBox;
	
	private ShapePanelRenderer actualShapeRenderer;
	private PhysicsControlPanelRenderer actualControlRenderer;
	
	private PhysicsControl control;
	
	public CollisionControlDialog(final Window owner) {
		dialog = new JDialog(owner, ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		dialog.getContentPane().setLayout(new BorderLayout(10, 10));
		
		final JPanel mainPanel = new JPanel(new BorderLayout());
		dialog.add(mainPanel, BorderLayout.CENTER);
		
		
		final JPanel typePanel = new JPanel(new BorderLayout());
		mainPanel.add(typePanel, BorderLayout.NORTH);
		
		final JPanel typeSelectPanel = new JPanel(new InputLayout());
		typePanel.add(typeSelectPanel, BorderLayout.NORTH);
		
		typeSelectPanel.add(new JLabel("Control type:"), InputLayout.LEFT);
		
		controlTypeBox = new JComboBox<>(ControlType.values());
		typeSelectPanel.add(controlTypeBox, InputLayout.RIGHT);
		
		final JPanel typeContentPanel = new JPanel();
		typeContentPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		typePanel.add(typeContentPanel, BorderLayout.CENTER);
		
		
		final JPanel shapePanel = new JPanel(new BorderLayout());
		mainPanel.add(shapePanel, BorderLayout.SOUTH);
		
		final JPanel shapeSelectPanel = new JPanel(new InputLayout());
		shapePanel.add(shapeSelectPanel, BorderLayout.NORTH);
		
		shapeSelectPanel.add(new JLabel("Shape type:"), InputLayout.LEFT);
		
		final JComboBox<ShapeType> shapeTypeBox = new JComboBox<>();
		shapeSelectPanel.add(shapeTypeBox, InputLayout.RIGHT);
		
		final JPanel shapeContentPanel = new JPanel();
		shapeContentPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		shapePanel.add(shapeContentPanel, BorderLayout.CENTER);
		
		
		final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		
		buttonPanel.add(new JButton(new AbstractAction("OK") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					final CollisionShape shape = actualShapeRenderer.getValue();
					control = actualControlRenderer.getValue(shape);
				} catch(final Throwable t) {
					JOptionPane.showMessageDialog(null, "Error: " + t.getMessage(), "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				dialog.setVisible(false);
			}
		}));
		
		controlTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				final ControlType type = (ControlType) controlTypeBox.getSelectedItem();
				if(type != null) {
					shapeTypeBox.setModel(new DefaultComboBoxModel<>(type.getShapeTypes()));
					
					actualControlRenderer = type.getRenderer();
					
					if(typeContentPanel.getComponentCount() > 0) {
						typeContentPanel.remove(typeContentPanel.getComponent(0));
					}
					typeContentPanel.add(actualControlRenderer.getPanel());
					
					typeContentPanel.repaint();
					dialog.pack();
					
					
					if(shapeTypeBox.getSelectedIndex() != -1) {
						shapeTypeBox.setSelectedIndex(-1);
					}
					shapeTypeBox.setSelectedIndex(0);
				}
			}
		});
		shapeTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				final ShapeType type = (ShapeType) shapeTypeBox.getSelectedItem();
				if(type != null) {
					actualShapeRenderer = type.getRenderer();
					if(shapeContentPanel.getComponentCount() > 0) {
						shapeContentPanel.remove(shapeContentPanel.getComponent(0));
					}
					shapeContentPanel.add(actualShapeRenderer.getPanel());
					dialog.pack();
				}
			}
		});
		
		dialog.pack();
	}
	public PhysicsControl show() {
		for(final ControlType type:ControlType.values()) {
			type.getRenderer().reset();
		}
		for(final ShapeType type:ShapeType.values()) {
			type.getRenderer().reset();
		}
		
		if(controlTypeBox.getSelectedIndex() != -1) {
			controlTypeBox.setSelectedIndex(-1);
		}
		controlTypeBox.setSelectedIndex(0);
		
		dialog.setLocationRelativeTo(dialog.getOwner());
		dialog.setVisible(true);
		
		final PhysicsControl c = control;
		control = null;
		return(c);
	}
	
	
	
	private static interface PanelRenderer {
		
		JPanel getPanel();
		
		void reset();
		
	}
	
	private static interface ShapePanelRenderer extends PanelRenderer {
		
		CollisionShape getValue();
		
	}
	
	private static interface PhysicsControlPanelRenderer extends PanelRenderer {
		
		public PhysicsControl  getValue(final CollisionShape shape);
		
	}
	
	// Needs permanently much memory
	private static final class BoxShapePanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField xField;
		private final JTextField yField;
		private final JTextField zField;
		
		public BoxShapePanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Extends along the X-Axis:"), InputLayout.LEFT);
			
			xField = new JTextField(15);
			panel.add(xField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Extends along the Y-Axis:"), InputLayout.LEFT);
			
			yField = new JTextField(15);
			panel.add(yField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Extends along the Z-Axis:"), InputLayout.LEFT);
			
			zField = new JTextField(15);
			panel.add(zField, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float x = Float.parseFloat(xField.getText());
			final float y = Float.parseFloat(yField.getText());
			final float z = Float.parseFloat(zField.getText());
			
			return(new BoxCollisionShape(x, y, z));
		}
		
		@Override
		public void reset() {
			xField.setText("0.5");
			yField.setText("0.5");
			zField.setText("0.5");
		}
	}
	private static final class Box2dShapePanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField xField;
		private final JTextField yField;
		
		public Box2dShapePanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Extends along the X-Axis:"), InputLayout.LEFT);
			
			xField = new JTextField(15);
			panel.add(xField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Extends along the Y-Axis:"), InputLayout.LEFT);
			
			yField = new JTextField(15);
			panel.add(yField, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float x = Float.parseFloat(xField.getText());
			final float y = Float.parseFloat(yField.getText());
			
			final Box2dShape shape = new Box2dShape(new Vector2f(x, y));
			return(shape);
		}
		
		@Override
		public void reset() {
			xField.setText("0.5");
			yField.setText("0.5");
		}
	}
	private static final class CapsuleShapePanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField rField;
		private final JTextField hField;
		
		public CapsuleShapePanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Radius:"), InputLayout.LEFT);
			
			rField = new JTextField(15);
			panel.add(rField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Height:"), InputLayout.LEFT);
			
			hField = new JTextField(15);
			panel.add(hField, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float r = Float.parseFloat(rField.getText());
			final float h = Float.parseFloat(hField.getText());
			
			final CapsuleCollisionShape shape = new CapsuleCollisionShape(r, h);
			return(shape);
		}
		
		@Override
		public void reset() {
			rField.setText("1.0");
			hField.setText("1.0");
		}
	}
	private static final class ConeShapePanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField rField;
		private final JTextField hField;
		
		public ConeShapePanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Radius:"), InputLayout.LEFT);
			
			rField = new JTextField(15);
			panel.add(rField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Height:"), InputLayout.LEFT);
			
			hField = new JTextField(15);
			panel.add(hField, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float r = Float.parseFloat(rField.getText());
			final float h = Float.parseFloat(hField.getText());
			
			return(new ConeCollisionShape(r, h));
		}
		
		@Override
		public void reset() {
			rField.setText("1.0");
			hField.setText("1.0");
		}
	}
	private static final class CylinderPanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField rField;
		private final JTextField hField;
		private final JComboBox<Axis> axisBox;
		
		public CylinderPanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Radius:"), InputLayout.LEFT);
			
			rField = new JTextField(15);
			panel.add(rField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Height:"), InputLayout.LEFT);
			
			hField = new JTextField(15);
			panel.add(hField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Axis of height:"), InputLayout.LEFT);
			
			axisBox = new JComboBox<>(Axis.values());
			axisBox.setSelectedItem(Axis.Y);
			panel.add(axisBox, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float r = Float.parseFloat(rField.getText());
			final float h = Float.parseFloat(hField.getText());
			final Axis axis = (Axis) axisBox.getSelectedItem();
			
			return(new CylinderCollisionShape(r, h, axis.ordinal()));
		}
		
		@Override
		public void reset() {
			rField.setText("1.0");
			hField.setText("1.0");
			axisBox.setSelectedIndex(0);
		}
	}
	private static final class EmptyShapePanelRenderer implements ShapePanelRenderer {
		@Override
		public JPanel getPanel() {
			return(new JPanel());
		}
		
		@Override
		public CollisionShape getValue() {
			return(new EmptyShape(false));
		}
		
		@Override
		public void reset() {
			
		}
	}
	private static final class MultiSpherePanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField rField;
		private final JTextField hField;
		
		public MultiSpherePanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Radius:"), InputLayout.LEFT);
			
			rField = new JTextField(15);
			panel.add(rField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Height:"), InputLayout.LEFT);
			
			hField = new JTextField(15);
			panel.add(hField, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float r = Float.parseFloat(rField.getText());
			final float h = Float.parseFloat(hField.getText());
			
			return(new MultiSphere(r, h));
		}
		
		@Override
		public void reset() {
			rField.setText("1.0");
			hField.setText("1.0");
		}
	}
	private static final class PlanePanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField normalXField;
		private final JTextField normalYField;
		private final JTextField normalZField;
		
		private final JTextField constantField;
		
		public PlanePanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Normal X:"), InputLayout.LEFT);
			
			normalXField = new JTextField(15);
			panel.add(normalXField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Normal Y:"), InputLayout.LEFT);
			
			normalYField = new JTextField(15);
			panel.add(normalXField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Normal Z:"), InputLayout.LEFT);
			
			normalZField = new JTextField(15);
			panel.add(normalZField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Constant:"), InputLayout.LEFT);
			
			constantField = new JTextField(15);
			panel.add(constantField, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float normalX = Float.parseFloat(normalXField.getText());
			final float normalY = Float.parseFloat(normalYField.getText());
			final float normalZ = Float.parseFloat(normalZField.getText());
			
			final float constant = Float.parseFloat(constantField.getText());
			
			return(new PlaneCollisionShape(new Plane(
					new Vector3f(normalX, normalY, normalZ),
					constant)
			));
		}
		
		@Override
		public void reset() {
			normalXField.setText("1.0");
			normalYField.setText("1.0");
			normalZField.setText("1.0");
			
			constantField.setText("1.0");
		}
	}
	private static final class SpherePanelRenderer implements ShapePanelRenderer {
		private final JPanel panel;
		
		private final JTextField rField;
		
		public SpherePanelRenderer() {
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Radius:"), InputLayout.LEFT);
			
			rField = new JTextField(15);
			panel.add(rField, InputLayout.RIGHT);
			
			reset();
		}
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public CollisionShape getValue() {
			final float r = Float.parseFloat(rField.getText());
			
			return(new SphereCollisionShape(r));
		}
		
		@Override
		public void reset() {
			rField.setText("1.0");
		}
	}
	
	private static final class GhostPanelRenderer implements PhysicsControlPanelRenderer {
		private final JPanel panel = new JPanel();
		
		public GhostPanelRenderer() {
			super();
		}
		
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public GhostControl getValue(final CollisionShape shape) {
			return(new GhostControl(shape));
		}
		
		@Override
		public void reset() {
		}
	}
	private static final class RigidPanelRenderer implements PhysicsControlPanelRenderer {
		private final JPanel panel;
		
		private final JTextField massField;
		private final JCheckBox kinematicBox;
		
		public RigidPanelRenderer() {
			super();
			
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Mass:"), InputLayout.LEFT);
			
			massField = new JTextField();
			panel.add(massField, InputLayout.RIGHT);
			
			panel.add(new JLabel("Kinematic:"), InputLayout.LEFT);
			
			kinematicBox = new JCheckBox();
			panel.add(kinematicBox, InputLayout.RIGHT);
			
			reset();
		}
		
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public PhysicsControl getValue(final CollisionShape shape) {
			final float mass = Float.parseFloat(massField.getText());
			
			final RigidBodyControl rigid = new RigidBodyControl(shape, mass);
			rigid.setKinematic(kinematicBox.isSelected());
			return(rigid);
		}
		
		@Override
		public void reset() {
			massField.setText("1.0");
			kinematicBox.setSelected(false);
		}
	}
	private static final class CharacterControlRenderer implements PhysicsControlPanelRenderer {
		private final JPanel panel;
		
		private final JTextField stepHeightField;
		
		public CharacterControlRenderer() {
			super();
			
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Step height:"), InputLayout.LEFT);
			
			stepHeightField = new JTextField();
			panel.add(stepHeightField, InputLayout.RIGHT);
			
			reset();
		}
		
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public PhysicsControl getValue(final CollisionShape shape) {
			final float stepHeight = Float.parseFloat(stepHeightField.getText());
			
			return(new CharacterControl((ConvexShape) shape, stepHeight));
		}
		
		@Override
		public void reset() {
			stepHeightField.setText("0.3");
		}
	}
	private static final class VehicleControlRenderer implements PhysicsControlPanelRenderer {
		private final JPanel panel;
		
		private final JTextField massField;
		
		public VehicleControlRenderer() {
			super();
			
			panel = new JPanel(new InputLayout(true, false));
			
			panel.add(new JLabel("Mass:"), InputLayout.LEFT);
			
			massField = new JTextField();
			panel.add(massField, InputLayout.RIGHT);
			
			reset();
		}
		
		@Override
		public JPanel getPanel() {
			return(panel);
		}
		
		@Override
		public PhysicsControl getValue(final CollisionShape shape) {
			final float mass = Float.parseFloat(massField.getText());
			return(new VehicleControl(shape, mass));
		}
		
		@Override
		public void reset() {
			massField.setText("1.0");
		}
	}
	
	private static enum ShapeType {
		BOX("Box", new BoxShapePanelRenderer()),
		BOX_2D("Box2D", new Box2dShapePanelRenderer()),
		CAPSULE("Capsule", new CapsuleShapePanelRenderer()),
		//COMPOUND,
		CONE("Cone", new ConeShapePanelRenderer()),
		//CONVEX_2D,
		CYLINDER("Cylinder", new CylinderPanelRenderer()),
		EMPTY("Empty", new EmptyShapePanelRenderer()),
		//G_IMPACT,
		//HULL,
		//MINKOWSKI_SUM,
		//MULTI_SPHERE("MultiSphere", new MultiSpherePanelRenderer()),
		PLANE("Plane", new PlanePanelRenderer()),
		//SIMPLE,
		SPHERE("Sphere", new SpherePanelRenderer());
		
		private final String name;
		private final ShapePanelRenderer renderer;
		
		private ShapeType(final String name, final ShapePanelRenderer renderer) {
			this.name = name;
			this.renderer = renderer;
		}
		@Override
		public String toString() {
			return(name);
		}
		
		public ShapePanelRenderer getRenderer() {
			return (renderer);
		}
	}
	private static enum ControlType {
		GHOST("GhostControl", new GhostPanelRenderer()) {
			@Override
			public ShapeType[] getShapeTypes() {
				return(ShapeType.values());
			}
		},
		RIGID("RigidBodyControl", new RigidPanelRenderer()) {
			@Override
			public ShapeType[] getShapeTypes() {
				return(ShapeType.values());
			}
		},
		CHARACTER_CONTROL("CharacterControl", new CharacterControlRenderer()) {
			@Override
			public ShapeType[] getShapeTypes() {
				return(new ShapeType[] {
						ShapeType.BOX, ShapeType.BOX_2D, ShapeType.CAPSULE,
						ShapeType.CONE, ShapeType.CYLINDER, ShapeType.SPHERE
				});
			}
		},
		/*BETTER_CHARACTER_CONTROL("BetterCharacterControl") {
			@Override
			public ShapeType[] getShapeTypes() {
				return(new ShapeType[] {
						ShapeType.CAPSULE
				});
			}
		},*/
		VEHICLE_CONTROL("VehicleControl", new VehicleControlRenderer()) {
			@Override
			public ShapeType[] getShapeTypes() {
				return(ShapeType.values());
			}
		};
		
		private final String name;
		private final PhysicsControlPanelRenderer renderer;
		
		private ControlType(final String name, final PhysicsControlPanelRenderer renderer) {
			this.name = name;
			this.renderer = renderer;
		}
		public PhysicsControlPanelRenderer getRenderer() {
			return(renderer);
		}
		
		@Override
		public String toString() {
			return(name);
		}
		public abstract ShapeType[] getShapeTypes();
	}
}
