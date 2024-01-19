package core.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import core.editables.Editable;
import core.editables.NodeEditable;
import core.editables.SpatialEditable;

public final class UiElementsStruct {
	private final JFrame topMenuWindow = new JFrame("Modeleditor");
	
	private final JMenuBar topMenuBar = new JMenuBar();
	
	private final JMenu topMenuFile = new JMenu("File");
	private final JMenuItem topMenuFileOpen = new JMenuItem("Open");
	private final JMenuItem topMenuFileSave = new JMenuItem("Save");
	private final JMenuItem topMenuFileClose = new JMenuItem("Close");
	private final JMenuItem topMenuFileExit = new JMenuItem("Exit");
	
	private final JMenu topMenuEdit = new JMenu("Edit");
	private final JMenuItem topMenuEditUndo = new JMenuItem("Undo");
	private final JMenuItem topMenuEditRedo = new JMenuItem("Redo");
	
	private final JMenu topMenuWindows = new JMenu("Windows");
	private final JMenuItem rearrangeWindowsCheckBox = new JMenuItem("Rearrange Windows");
	private final JCheckBox threeDViewVisibilityCheckBox = new JCheckBox("3D view", true);
	private final JCheckBox propertiesVisibilityCheckBox = new JCheckBox("Properties", true);
	private final JCheckBox sceneGraphVisibilityCheckBox = new JCheckBox("Scene graph", true);
	private final JCheckBox filesVisibilityCheckBox = new JCheckBox("Files", true);
	private final JCheckBox cameraVisibilityCheckBox = new JCheckBox("Camera", true);
	private final JCheckBox threeDTransformVisibilityCheckBox = new JCheckBox("3D transform", true);
	
	private final JToolBar toolbar = new JToolBar();
	private final JButton toolbarOpen = new JButton("Open");
	private final JButton toolbarSave = new JButton("Save");
	private final JButton toolbarClose = new JButton("Close");
	private final JToggleButton toolbarAmbientLightButton = new JToggleButton("Ambient light");
	private final ButtonGroup toolbarGridButtonGroup = new ButtonGroup();
	private final JToggleButton noGridButton = new JToggleButton("No grid");
	private final JToggleButton moveByGridButton = new JToggleButton("Move by grid");
	private final JSlider gridSizeSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 1000, 100);
	private final JTextField gridSizeField = new JTextField(5);
	
	private final JDialog sceneGraphDialog = new JDialog(topMenuWindow, "Scene graph", false);
	private final JTree sceneGraphTree = new JTree(new DefaultTreeModel(null));
	
	private final JDialog propertiesDialog = new JDialog(topMenuWindow, "Properties", false);
	private final JTable propertiesTable = new ActualTypeTable();
	
	private final JDialog filesDialog = new JDialog(topMenuWindow, "Files", false);
	
	private final JDialog cameraWindow = new JDialog(topMenuWindow, "Camera", false);
	private final JToolBar cameraToolbar = new JToolBar();
	private final ButtonGroup cameraButtonGroup = new ButtonGroup();
	private final JToggleButton cameraFlyByCamButton = new JToggleButton("FlyByCam");
	private final JToggleButton cameraChaseCamButton = new JToggleButton("ChaseCamera");
	private final JButton cameraToOriginButton = new JButton("To origin");
	private final JButton cameraToSelectedButton = new JButton("To selected");
	
	private final JDialog threeDTransformWindow = new JDialog(topMenuWindow, "3D transform", false);
	private final JToolBar threeDTransformToolbar = new JToolBar();
	private final ButtonGroup threeDTransformButtonGroup = new ButtonGroup();
	private final JToggleButton threeDTransformNoneButton = new JToggleButton("None");
	
	public UiElementsStruct() {
		setup();
	}
	
	private void setup() {
		setupTopMenuWindow();
		setupPropertiesWindow();
		setupSceneGraphWindow();
		setupFilesWindow();
		setupCameraWindow();
		setupThreeDTransformWindow();
	}
	private void setupTopMenuWindow() {
		topMenuWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		topMenuWindow.setJMenuBar(topMenuBar);
		
		topMenuBar.add(topMenuFile);
		
		topMenuFileOpen.setMnemonic(KeyEvent.VK_O);
		topMenuFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		topMenuFile.add(topMenuFileOpen);
		
		topMenuFileSave.setMnemonic(KeyEvent.VK_S);
		topMenuFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		topMenuFileSave.setEnabled(false);
		topMenuFile.add(topMenuFileSave);
		
		topMenuFile.addSeparator();
		
		topMenuFileClose.setEnabled(false);
		topMenuFile.add(topMenuFileClose);
		
		topMenuFileExit.setMnemonic(KeyEvent.VK_Q);
		topMenuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		topMenuFile.add(topMenuFileExit);
		
		topMenuBar.add(topMenuEdit);
		
		topMenuEditUndo.setEnabled(false);
		topMenuEditUndo.setMnemonic(KeyEvent.VK_Z);
		topMenuEditUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		topMenuEdit.add(topMenuEditUndo);
		
		topMenuEditRedo.setEnabled(false);
		topMenuEditRedo.setMnemonic(KeyEvent.VK_Y);
		topMenuEditRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		topMenuEdit.add(topMenuEditRedo);
		
		topMenuBar.add(topMenuWindows);
		
		topMenuWindows.add(rearrangeWindowsCheckBox);
		topMenuWindows.addSeparator();
		topMenuWindows.add(threeDViewVisibilityCheckBox);
		topMenuWindows.add(sceneGraphVisibilityCheckBox);
		topMenuWindows.add(propertiesVisibilityCheckBox);
		topMenuWindows.add(filesVisibilityCheckBox);
		topMenuWindows.add(cameraVisibilityCheckBox);
		topMenuWindows.add(threeDTransformVisibilityCheckBox);
		
		toolbar.setFloatable(false);
		topMenuWindow.add(toolbar, BorderLayout.NORTH);
		
		toolbar.add(toolbarOpen);
		
		toolbarSave.setEnabled(false);
		toolbar.add(toolbarSave);
		
		toolbarClose.setEnabled(false);
		toolbar.add(toolbarClose);
		
		toolbar.addSeparator();
		
		toolbar.add(toolbarAmbientLightButton);
		
		toolbar.addSeparator();
		
		toolbar.add(noGridButton);
		toolbarGridButtonGroup.add(noGridButton);
		
		toolbar.add(moveByGridButton);
		toolbarGridButtonGroup.add(moveByGridButton);
		
		gridSizeSlider.setPaintTicks(true);
		gridSizeSlider.setSnapToTicks(true);
		gridSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				gridSizeField.setText(String.valueOf(gridSizeSlider.getValue() / 100.0f));
			}
		});
		toolbar.add(gridSizeSlider);
		
		gridSizeField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					final String text = gridSizeField.getText();
					try {
						final float f = Float.parseFloat(text);
						gridSizeSlider.setValue(Math.round(f * 100));
					} catch(final NumberFormatException e1) {
						JOptionPane.showMessageDialog(topMenuWindow, "Could not parse float: " + e1);
					}
				}
			}
		});
		toolbar.add(gridSizeField);
	}
	private void setupPropertiesWindow() {
		propertiesDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		propertiesVisibilityCheckBox.addChangeListener(new ChangeListener() {
			private boolean beforeSelected = true;
			
			@Override
			public void stateChanged(final ChangeEvent e) {
				final boolean selected = propertiesVisibilityCheckBox.isSelected();
				if(beforeSelected != selected) {
					propertiesDialog.setVisible(selected);
					beforeSelected = selected;
				}
			}
		});
		propertiesDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				propertiesVisibilityCheckBox.setSelected(false);
			}
		});
		
		propertiesTable.setDefaultEditor(Float.class, new FloatTableEditor());
		propertiesTable.setDefaultEditor(Integer.class, new IntegerTableEditor());
		propertiesTable.setDefaultEditor(Boolean.class, new DefaultCellEditor(new JCheckBox()) {
			@Override
			public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row,
					final int column) {
				final JCheckBox comp = (JCheckBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
				comp.setSelected(Boolean.parseBoolean(Objects.toString(value)));
				return(comp);
			}
		});
		propertiesTable.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
					final boolean hasFocus, final int row, final int column) {
				final JCheckBox box = new JCheckBox();
				box.setSelected(Boolean.parseBoolean(Objects.toString(value)));
				return(box);
			}
		});
		propertiesTable.setDefaultRenderer(Vector3f.class, new Vector3fTableRenderer());
		propertiesTable.setDefaultEditor(Vector3f.class, new Vector3fTableEditor());
		propertiesTable.setDefaultRenderer(Quaternion.class, new QuaternionTableRenderer());
		propertiesTable.setDefaultEditor(Quaternion.class, new QuaternionTableEditor());
		
		propertiesTable.getTableHeader().setReorderingAllowed(false);
		propertiesTable.setEnabled(false);
		
		propertiesDialog.add(new JScrollPane(propertiesTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}
	private void setupSceneGraphWindow() {
		sceneGraphDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		sceneGraphVisibilityCheckBox.addChangeListener(new ChangeListener() {
			private boolean beforeSelected = true;
			
			@Override
			public void stateChanged(final ChangeEvent e) {
				final boolean selected = sceneGraphVisibilityCheckBox.isSelected();
				if(beforeSelected != selected) {
					sceneGraphDialog.setVisible(selected);
					beforeSelected = selected;
				}
			}
		});
		sceneGraphDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				sceneGraphVisibilityCheckBox.setSelected(false);
			}
		});
		
		sceneGraphTree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded,
					final boolean leaf, final int row, final boolean hasFocus) {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				final Editable editable = (Editable) node.getUserObject();
				return(super.getTreeCellRendererComponent(tree, value, sel, expanded, editable != null ? !(editable instanceof NodeEditable) : leaf, row, hasFocus));
			}
		});
		sceneGraphTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				final TreePath path = sceneGraphTree.getSelectionPath();
				if(path == null) {
					return;
				}
				final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				cameraToSelectedButton.setEnabled(selectedNode.getUserObject() instanceof SpatialEditable);
			}
		});
		sceneGraphDialog.add(new JScrollPane(sceneGraphTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}
	private void setupFilesWindow() {
		filesDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		filesVisibilityCheckBox.addChangeListener(new ChangeListener() {
			private boolean beforeSelected = true;
			
			@Override
			public void stateChanged(final ChangeEvent e) {
				final boolean selected = filesVisibilityCheckBox.isSelected();
				if(beforeSelected != selected) {
					filesDialog.setVisible(selected);
					beforeSelected = selected;
				}
			}
		});
		filesDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				filesVisibilityCheckBox.setSelected(false);
			}
		});
	}
	private void setupCameraWindow() {
		cameraWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		cameraVisibilityCheckBox.addChangeListener(new ChangeListener() {
			private boolean beforeSelected = true;
			
			@Override
			public void stateChanged(final ChangeEvent e) {
				final boolean selected = cameraVisibilityCheckBox.isSelected();
				if(beforeSelected != selected) {
					cameraWindow.setVisible(selected);
					beforeSelected = selected;
				}
			}
		});
		cameraWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				cameraVisibilityCheckBox.setSelected(false);
			}
		});
		
		cameraToolbar.setFloatable(false);
		cameraWindow.add(cameraToolbar);
		
		cameraFlyByCamButton.setSelected(true);
		cameraButtonGroup.add(cameraFlyByCamButton);
		cameraToolbar.add(cameraFlyByCamButton);
		
		cameraButtonGroup.add(cameraChaseCamButton);
		cameraToolbar.add(cameraChaseCamButton);
		
		cameraToolbar.addSeparator();
		
		cameraToolbar.add(cameraToOriginButton);
		cameraToSelectedButton.setEnabled(false);
		cameraToolbar.add(cameraToSelectedButton);
	}
	private void setupThreeDTransformWindow() {
		threeDTransformWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		threeDTransformVisibilityCheckBox.addChangeListener(new ChangeListener() {
			private boolean beforeSelected = true;
			
			@Override
			public void stateChanged(final ChangeEvent e) {
				final boolean selected = threeDTransformVisibilityCheckBox.isSelected();
				if(beforeSelected != selected) {
					threeDTransformWindow.setVisible(selected);
					beforeSelected = selected;
				}
			}
		});
		threeDTransformWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				threeDTransformVisibilityCheckBox.setSelected(false);
			}
		});
		
		threeDTransformToolbar.setFloatable(false);
		threeDTransformWindow.add(threeDTransformToolbar);
		
		threeDTransformButtonGroup.add(threeDTransformNoneButton);
		//threeDTransformNoneButton.setEnabled(false);
		threeDTransformToolbar.add(threeDTransformNoneButton);
	}
	
	public JFrame getTopMenuWindow() {
		return (topMenuWindow);
	}
	
	public JMenuBar getTopMenuBar() {
		return (topMenuBar);
	}
	public JMenu getTopMenuFile() {
		return (topMenuFile);
	}
	public JMenuItem getTopMenuFileOpen() {
		return (topMenuFileOpen);
	}
	public JMenuItem getTopMenuFileSave() {
		return (topMenuFileSave);
	}
	public JMenuItem getTopMenuFileClose() {
		return (topMenuFileClose);
	}
	public JMenuItem getTopMenuFileExit() {
		return (topMenuFileExit);
	}
	
	public JMenu getTopMenuEdit() {
		return (topMenuEdit);
	}
	public JMenuItem getTopMenuEditRedo() {
		return (topMenuEditRedo);
	}
	public JMenuItem getTopMenuEditUndo() {
		return (topMenuEditUndo);
	}
	
	public JMenu getTopMenuWindows() {
		return (topMenuWindows);
	}
	public JMenuItem getRearrangeWindowsMenuItem() {
		return (rearrangeWindowsCheckBox);
	}
	public JCheckBox getThreeDViewVisibilityCheckBox() {
		return (threeDViewVisibilityCheckBox);
	}
	
	public JToolBar getToolbar() {
		return (toolbar);
	}
	public JButton getToolbarOpen() {
		return (toolbarOpen);
	}
	public JButton getToolbarSave() {
		return (toolbarSave);
	}
	public JButton getToolbarClose() {
		return (toolbarClose);
	}
	public JToggleButton getToolbarAmbientLightButton() {
		return (toolbarAmbientLightButton);
	}
	public ButtonGroup getToolbarGridButtonGroup() {
		return (toolbarGridButtonGroup);
	}
	public JToggleButton getNoGridButton() {
		return (noGridButton);
	}
	public JToggleButton getMoveByGridButton() {
		return (moveByGridButton);
	}
	public JSlider getGridSizeSlider() {
		return (gridSizeSlider);
	}
	public JTextField getGridSizeField() {
		return (gridSizeField);
	}
	
	public JDialog getSceneGraphWindow() {
		return (sceneGraphDialog);
	}
	public JTree getSceneGraphTree() {
		return (sceneGraphTree);
	}
	
	public JDialog getPropertiesWindow() {
		return (propertiesDialog);
	}
	public JTable getPropertiesTable() {
		return (propertiesTable);
	}
	
	public JDialog getFilesWindow() {
		return(filesDialog);
	}
	
	public JDialog getCameraWindow() {
		return (cameraWindow);
	}
	public JToolBar getCameraToolbar() {
		return (cameraToolbar);
	}
	public ButtonGroup getCameraButtonGroup() {
		return (cameraButtonGroup);
	}
	public JToggleButton getCameraFlyByCamButton() {
		return (cameraFlyByCamButton);
	}
	public JToggleButton getCameraChaseCamButton() {
		return (cameraChaseCamButton);
	}
	public JButton getCameraToOriginButton() {
		return (cameraToOriginButton);
	}
	public JButton getCameraToSelectedButton() {
		return (cameraToSelectedButton);
	}
	
	public JDialog getThreeDTransformWindow() {
		return (threeDTransformWindow);
	}
	public JToolBar getThreeDTransformToolbar() {
		return (threeDTransformToolbar);
	}
	public ButtonGroup getThreeDTransformButtonGroup() {
		return (threeDTransformButtonGroup);
	}
	public JToggleButton getThreeDTransformNoneButton() {
		return (threeDTransformNoneButton);
	}
}
