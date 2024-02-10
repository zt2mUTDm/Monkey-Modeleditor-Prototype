package core.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.system.lwjgl.LwjglDisplay;

import cams.ChaseCameraEditorState;
import cams.EditorCamera;
import cams.FlyByCamEditorState;
import core.Utils;
import core.editables.AnimComposerActionEditable;
import core.editables.AnimComposerEditable;
import core.editables.AnimControlEditable;
import core.editables.AnimationControl;
import core.editables.AnimationControlEditable;
import core.editables.AnimationControlMappingEditable;
import core.editables.BoxMeshEditable;
import core.editables.CollisionShapeEditable;
import core.editables.ControlEditable;
import core.editables.CylinderMeshEditable;
import core.editables.DomeMeshEditable;
import core.editables.Editable;
import core.editables.EditionMode;
import core.editables.GeometryEditable;
import core.editables.MeshEditable;
import core.editables.NodeEditable;
import core.editables.PhysicsControlEditable;
import core.editables.SkinningControlEditable;
import core.editables.SpatialEditable;
import core.editables.SphereMeshEditable;
import core.editables.TorusMeshEditable;
import core.setting.ModelSetting;
import core.threed.EditionState;
import core.threed.EditionStateModel;
import core.ui.DirectoryTree;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.ValueChangedHandler;
import online.money_daisuki.api.base.models.MutableDequeModel;
import online.money_daisuki.api.base.models.MutableDequeModelImpl;
import online.money_daisuki.api.base.models.MutableSingleValueModel;
import online.money_daisuki.api.base.models.MutableSingleValueModelImpl;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModel;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class SwingUi {
	private final SetableMutableSingleValueModel<File> openedFolder = new SetableMutableSingleValueModelImpl<>();
	private final SetableMutableSingleValueModel<File> openedFile = new SetableMutableSingleValueModelImpl<>();
	
	private final MutableSingleValueModel<Boolean> openFileChanged = new MutableSingleValueModelImpl<>(Boolean.FALSE);
	
	private final MutableDequeModel<Mapping<Runnable, Runnable>> undos = new MutableDequeModelImpl<>();
	private final MutableDequeModel<Mapping<Runnable, Runnable>> redos = new MutableDequeModelImpl<>();
	
	private DefaultTreeModel fileSelectionTreeModel;
	
	private ThreeDimensionalView tdview;
	
	private List<EditionMode> selectedObjectsEditorModes;
	
	private EditionMode selectedMode;
	
	private final SelectionModel<Editable> selectionModel = new SelectionModelImpl<>();
	
	private final ModelSetting setting;
	private final SimpleApplication app;
	
	private final MutableSingleValueModel<GridMode> gridMode = new MutableSingleValueModelImpl<>(GridMode.NONE);
	
	private final UiElementsStruct ui;
	
	public SwingUi(final SimpleApplication app, final ModelSetting setting) {
		this.app = Requires.notNull(app, "app == null");
		this.setting = Requires.notNull(setting, "setting == null");
		
		ui = new UiElementsStruct();
		
		setupTopMenuWindow();
		setThreeDWindow();
		setupSceneGraphWindow();
		setupPropertiesWindow();
		setupCameraWindow();
		setupFilesWindow();
		setupThreeDTransform();
		
		boolean allWindowsAt00 = true;
		for(final Window window:getAllWindows()) {
			allWindowsAt00&= window.getX() == 0 && window.getY() == 0;
		}
		
		if(allWindowsAt00) {
			rearrangeWindows();
		}
	}
	
	private void setupTopMenuWindow() {
		ui.getTopMenuWindow().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				requestExit();
			}
		});
		
		ui.getTopMenuWindow().setSize(setting.getTopMenuWindowSize());
		setWindowLocation(ui.getTopMenuWindow(), setting.getTopMenuWindowLocation());
		
		ui.getTopMenuWindow().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(final ComponentEvent e) {
				setting.setTopWindowLocation(ui.getTopMenuWindow().getLocation());
			}
			@Override
			public void componentResized(final ComponentEvent e) {
				setting.setTopWindowSize(ui.getTopMenuWindow().getSize());
			}
		});
		
		ui.getTopMenuFileSave().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				saveFile();
			}
		});
		
		openFileChanged.addValueChangedHandler(new ValueChangedHandler<Boolean>() {
			@Override
			public void valueChanged(final Boolean oldValue, final Boolean newValue) {
				ui.getTopMenuFileSave().setEnabled(newValue);
			}
		});
		openFileChanged.addValueChangedHandler(new ValueChangedHandler<Boolean>() {
			@Override
			public void valueChanged(final Boolean oldValue, final Boolean newValue) {
				final JFrame frame = ui.getTopMenuWindow();
				if(newValue) {
					String title = frame.getTitle();
					if(!title.endsWith("*")) {
						title+= "*";
						frame.setTitle(title);
					}
				} else {
					String title = frame.getTitle();
					if(title.endsWith("*")) {
						title = title.substring(0, title.length() - 1);
						frame.setTitle(title);
					}
				}
			}
		});
		
		ui.getTopMenuFileClose().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				requestCloseFolder();
			}
		});
		openedFolder.addValueChangedHandler(new ValueChangedHandler<File>() {
			@Override
			public void valueChanged(final File oldVersion, final File newVersion) {
				ui.getTopMenuFileClose().setEnabled(newVersion != null);
			}
		});
		
		ui.getTopMenuFileExit().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				requestExit();
			}
		});
		
		
		ui.getTopMenuEditUndo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				undo();
			}
		});
		undos.addChangeListener(new DataSink<MutableDequeModel<Mapping<Runnable, Runnable>>>() {
			@Override
			public void sink(final MutableDequeModel<Mapping<Runnable, Runnable>> value) {
				ui.getTopMenuEditUndo().setEnabled(!undos.isEmpty());
			}
		});
		ui.getTopMenuEditRedo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				redo();
			}
		});
		redos.addChangeListener(new DataSink<MutableDequeModel<Mapping<Runnable, Runnable>>>() {
			@Override
			public void sink(final MutableDequeModel<Mapping<Runnable, Runnable>> value) {
				ui.getTopMenuEditRedo().setEnabled(!redos.isEmpty());
			}
		});
		
		
		ui.getRearrangeWindowsMenuItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				rearrangeWindows();
			}
		});
		
		/*app.enqueue(new Runnable() {
			@Override
			public void run() {
				/*final AwtPanel panel = ((AwtPanelsContext) app.getContext()).createPanel(PaintMode.Accelerated, true);
				((AwtPanelsContext) app.getContext()).setInputSource(panel);
				
				panel.attachTo(true, app.getViewPort());*/
				
				/*EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						//ui.getTopMenuWindow().add(panel);
					}
				});
			}
		});*/
		
		final long windowHandle = ((LwjglDisplay)app.getContext()).getWindowHandle();
		
		ui.getThreeDViewVisibilityCheckBox().addChangeListener(new ChangeListener() {
			private boolean oldSelection = true;
			
			@Override
			public void stateChanged(final ChangeEvent e) {
				final boolean selected = ui.getThreeDViewVisibilityCheckBox().isSelected();
				if(oldSelection != selected) {
					if(selected) {
						GLFW.glfwShowWindow(windowHandle);
					} else {
						GLFW.glfwHideWindow(windowHandle);
					}
					oldSelection = selected;
				}
			}
		});
		
		GLFW.glfwSetWindowCloseCallback(windowHandle, new GLFWWindowCloseCallbackI() {
			@Override
			public void invoke(final long arg0) {
				GLFW.glfwSetWindowShouldClose(windowHandle, false);
				ui.getThreeDViewVisibilityCheckBox().setSelected(false);
			}
		});
		
		
		
		ui.getToolbarSave().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				saveFile();
			}
		});
		openFileChanged.addValueChangedHandler(new ValueChangedHandler<Boolean>() {
			@Override
			public void valueChanged(final Boolean oldValue, final Boolean newValue) {
				ui.getToolbarSave().setEnabled(newValue.booleanValue());
			}
		});
		ui.getToolbarClose().addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				requestCloseFolder();
			}
		});
		openedFolder.addValueChangedHandler(new ValueChangedHandler<File>() {
			@Override
			public void valueChanged(final File old, final File nevv) {
				ui.getToolbarClose().setEnabled(nevv != null);
			}
		});
		
		ui.getToolbarAmbientLightButton().addItemListener(new ItemListener() {
			private final AmbientLight light = new AmbientLight();
			
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if(ui.getToolbarAmbientLightButton().isSelected()) {
					app.enqueue(new Runnable() {
						@Override
						public void run() {
							app.getRootNode().addLight(light);
						}
					});
				} else {
					app.enqueue(new Runnable() {
						@Override
						public void run() {
							app.getRootNode().removeLight(light);
						}
					});
				}
			}
		});
		
		
		gridMode.addValueChangedHandler(new ValueChangedHandler<GridMode>() {
			@Override
			public void valueChanged(final GridMode oldValue, final GridMode newValue) {
				setting.setGridMode(newValue);
			}
		});
		
		final GridMode mode = setting.getGridMode();
		
		ui.getNoGridButton().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if(ui.getNoGridButton().isSelected()) {
					gridMode.sink(GridMode.NONE);
				}
			}
		});
		ui.getNoGridButton().setSelected(mode == GridMode.NONE);
		
		ui.getMoveByGridButton().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if(ui.getMoveByGridButton().isSelected()) {
					gridMode.sink(GridMode.MOVE_BY);
				}
			}
		});
		ui.getMoveByGridButton().setSelected(mode == GridMode.MOVE_BY);
		
		
		final int preGridSize = setting.getGridSize();
		
		ui.getGridSizeSlider().setValue(preGridSize);
		ui.getGridSizeSlider().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				setting.setGridSize(ui.getGridSizeSlider().getValue());
			}
		});
		
		ui.getGridSizeField().setText(String.valueOf(preGridSize / 100.0f));
	}
	private void setThreeDWindow() {
		tdview = new ThreeDimensionalView(app);
		tdview.setSelectionModel(selectionModel);
	}
	private void setupSceneGraphWindow() {
		ui.getSceneGraphWindow().setSize(setting.getSceneGraphWindowSize());
		setWindowLocation(ui.getSceneGraphWindow(), setting.getSceneGraphWindowLocation());
		
		ui.getSceneGraphWindow().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(final ComponentEvent e) {
				setting.setSceneGraphWindowLocation(ui.getSceneGraphWindow().getLocation());
			}
			@Override
			public void componentResized(final ComponentEvent e) {
				setting.setSceneGraphWindowSize(ui.getSceneGraphWindow().getSize());
			}
		});
		
		final MutableSingleValueModel<Boolean> updateTreeSelection = new MutableSingleValueModelImpl<>(Boolean.TRUE);
		
		final SelectionListener<Editable> updateComponentTreeSelectionListener = new SelectionListener<Editable>() {
			@Override
			public void selectionAdded(final Object source, final Editable value) {
				value.setSelected(true);
				
				if(updateTreeSelection.source()) {
					final JTree tree = ui.getSceneGraphTree();
					final DefaultMutableTreeNode node = searchTreeNodeForUserObject((DefaultMutableTreeNode)tree.getModel().getRoot(), value);
					
					if(node != null) {
						final TreeNode[] nodes = ((DefaultTreeModel)tree.getModel()).getPathToRoot(node);
						final TreePath[] curPaths = tree.getSelectionPaths();
						if(curPaths == null) {
							tree.setSelectionPath(new TreePath(nodes));
						} else {
							final int len = curPaths.length;
							
							final TreePath[] newPaths = new TreePath[len + 1];
							System.arraycopy(curPaths, 0, newPaths, 0, len);
							newPaths[len] = new TreePath(nodes);
							tree.setSelectionPaths(newPaths);
						}
					}
				}
			}
			@Override
			public void selectionRemoved(final Object source, final Editable value) {
				value.setSelected(false);
				
				if(updateTreeSelection.source()) {
					final JTree tree = ui.getSceneGraphTree();
					final DefaultMutableTreeNode node = searchTreeNodeForUserObject((DefaultMutableTreeNode) tree.getModel().getRoot(), value);
					
					if(node != null) {
						final TreeNode[] nodes = ((DefaultTreeModel)tree.getModel()).getPathToRoot(node);
						final TreePath[] curPaths = tree.getSelectionPaths();
						if(curPaths == null) {
							return;
						} else {
							final Collection<TreePath> curPathCol = new ArrayList<>(Arrays.asList(curPaths));
							curPathCol.remove(new TreePath(nodes));
							tree.setSelectionPaths(curPathCol.toArray(new TreePath[curPathCol.size()]));
						}
					}
				}
			}
			private DefaultMutableTreeNode searchTreeNodeForUserObject(final DefaultMutableTreeNode node, final Object userObject) {
				if(Objects.equals(node.getUserObject(), userObject)) {
					return(node);
				}
				
				for(int i = 0, size = node.getChildCount(); i < size; i++) {
					final DefaultMutableTreeNode result = searchTreeNodeForUserObject((DefaultMutableTreeNode) node.getChildAt(i), userObject);
					if(result != null) {
						return(result);
					}
				}
				return(null);
			}
		};
		
		ui.getSceneGraphTree().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				updateTreeSelection.sink(Boolean.FALSE);
				
				selectionModel.clear();
				
				final TreePath[] paths = ui.getSceneGraphTree().getSelectionPaths();
				if(paths != null) {
					for(final TreePath path:paths) {
						final DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
						final Editable obj = (Editable) node.getUserObject();
						selectionModel.add(obj);
					}
				} else {
					selectionModel.clear();
				}
				
				updateTreeSelection.sink(Boolean.TRUE);
			}
		});
		selectionModel.addSelectionChangedListener(updateComponentTreeSelectionListener);
		
		
		ui.getSceneGraphTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					final JTree tree = ui.getSceneGraphTree();
					final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					if(path == null) {
						return;
					}
					
					tree.setSelectionPath(path);
					
					final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
					final Editable obj = (Editable) selectedNode.getUserObject();
					
					final boolean isSpatial = obj instanceof SpatialEditable;
					final boolean isNode = obj instanceof NodeEditable;
					final boolean isControl = obj instanceof ControlEditable;
					
					final JPopupMenu menu = new JPopupMenu();
					
					final JMenu addMenu = new JMenu("Add");
					menu.add(addMenu);
					
					final JMenuItem newNodeItem = new JMenuItem(new AbstractAction("Node") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final NodeEditable newNode = NodeEditable.valueOf(app, new Node(""));
							newNode.setName("New node");
							createEditable(newNode, selectedNode);
						}
					});
					newNodeItem.setEnabled(isNode);
					addMenu.add(newNodeItem);
					
					/*addMenu.add(new JMenuItem(new AbstractAction("AudioNode") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							/*final AudioNodeEditable newNode = new AudioNodeEditable(app, "New audio node");
							createEditable(newNode, selectedNode);
							openFileChanged.sink(Boolean.TRUE);*/
						/*}
					}));*/
					
					addMenu.addSeparator();
					
					
					final JMenuItem importModelItem = new JMenuItem(new AbstractAction("Import model") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final JFileChooser chooser = new JFileChooser(setting.getLastLoadedModelPath());
							if(chooser.showOpenDialog(ui.getTopMenuWindow()) == JFileChooser.APPROVE_OPTION) {
								final File selectedFile = chooser.getSelectedFile();
								setting.setLastLoadedModelPath(selectedFile.getAbsolutePath());
								
								final String parentPath = selectedFile.getParentFile().getPath();
								
								app.getAssetManager().registerLocator(parentPath, FileLocator.class);
								
								final Spatial spatial = app.getAssetManager().loadModel(selectedFile.getName());
								createEditable(SpatialEditable.valueOf(app, spatial), selectedNode);
								
								app.getAssetManager().unregisterLocator(parentPath, FileLocator.class);
								
								openFileChanged.sink(Boolean.TRUE);
							}
						}
					});
					importModelItem.setEnabled(isNode);
					addMenu.add(importModelItem);
					
					addMenu.addSeparator();
					
					final JMenuItem addBoxItem = new JMenuItem(new AbstractAction("Box") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Box box = new Box(0.5f, 0.5f, 0.5f);
							final BoxMeshEditable boxEditable = new BoxMeshEditable(box);
							
							final Geometry geo = new Geometry("New box", boxEditable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, boxEditable);
							
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					});
					addBoxItem.setEnabled(isNode);
					addMenu.add(addBoxItem);
					
					final JMenuItem addCylinderItem = new JMenuItem(new AbstractAction("Cylinder") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Cylinder cylinder = new Cylinder(8, 32, 0.5f, 1, true, false);
							final MeshEditable editable = new CylinderMeshEditable(cylinder);
							
							final Geometry geo = new Geometry("New cylinder", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					});
					addCylinderItem.setEnabled(isNode);
					addMenu.add(addCylinderItem);
					
					final JMenuItem addSphereItem = new JMenuItem(new AbstractAction("Sphere") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Sphere sphere = new Sphere(16, 32, 1);
							final MeshEditable editable = new SphereMeshEditable(sphere);
							
							final Geometry geo = new Geometry("New sphere", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					});
					addSphereItem.setEnabled(isNode);
					addMenu.add(addSphereItem);
					
					final JMenuItem addDomeItem = new JMenuItem(new AbstractAction("Dome") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Dome dome = new Dome(8, 16, 1);
							final MeshEditable editable = new DomeMeshEditable(dome);
							
							final Geometry geo = new Geometry("New dome", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					});
					addDomeItem.setEnabled(isNode);
					addMenu.add(addDomeItem);
					
					addMenu.add(new JMenuItem(new AbstractAction("Cone") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Dome dome = new Dome(2, 16, 1);
							final MeshEditable editable = new DomeMeshEditable(dome);
							
							final Geometry geo = new Geometry("New cone", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					addMenu.add(new JMenuItem(new AbstractAction("Tetrahedron") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Dome dome = new Dome(2, 3, 1);
							final MeshEditable editable = new DomeMeshEditable(dome);
							
							final Geometry geo = new Geometry("New tetrahedron", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					addMenu.add(new JMenuItem(new AbstractAction("Pyramid") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Dome dome = new Dome(2, 4, 1);
							final MeshEditable editable = new DomeMeshEditable(dome);
							
							final Geometry geo = new Geometry("New pyramid", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					addMenu.add(new JMenuItem(new AbstractAction("Torus") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Torus torus = new Torus(16, 32, 0.33f, 1f);
							final MeshEditable editable = new TorusMeshEditable(torus);
							
							final Geometry geo = new Geometry("New torus", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = GeometryEditable.valueOf(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					
					addMenu.addSeparator();
					
					final JMenuItem addNodeAsFirstItem = new JMenuItem("Add node as root");
					addNodeAsFirstItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(final ActionEvent e) {
							
						}
					});
					addNodeAsFirstItem.setEnabled(false);
					addMenu.add(addNodeAsFirstItem);
					
					
					final JMenuItem makeChildToRootNode = new JMenuItem("Make child to root node");
					makeChildToRootNode.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(final ActionEvent e) {
							
						}
					});
					makeChildToRootNode.setEnabled(false);
					addMenu.add(makeChildToRootNode);
					
					addMenu.addSeparator();
					
					final JMenuItem addActionLinks = new JMenuItem("Map string to action");
					addActionLinks.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final String[] mapping = new StringInputDialog(null, "", new String[] {
									"Internal action name:",
									"Model action name:"
							}).show();
							
							if(mapping == null) {
								return;
							}
							
							final String a = mapping[0];
							final String b = mapping[1];
							
							if(a.isEmpty() || b.isEmpty()) {
								return;
							}
							
							final AnimationControlEditable ace = (AnimationControlEditable) obj;
							final AnimationControl control = (AnimationControl) ace.getControl();
							
							final Runnable refreshTreeRun = new Runnable() {
								@Override
								public void run() {
									rebuildAnimationControlPath(selectedNode);
									ui.getSceneGraphTreeModel().nodeStructureChanged(selectedNode);
								}
							};
							execute(new FinalMapping<>(
									new Runnable() {
										@Override
										public void run() {
											control.addActionMapping(a, b);
											EventQueue.invokeLater(refreshTreeRun);
										}
									},
									new Runnable() {
										@Override
										public void run() {
											control.removeActionMapping(a, b);
											EventQueue.invokeLater(refreshTreeRun);
										}
									}
							));
						}
					});
					addActionLinks.setEnabled(obj instanceof AnimationControlEditable);
					addMenu.add(addActionLinks);
					
					addMenu.addSeparator();
					
					final JMenuItem addCollisionControl = new JMenuItem("Collision control");
					addCollisionControl.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final PhysicsControl control = new CollisionControlDialog(null).show();
							
							if(control != null) {
								final PhysicsControlEditable edit = PhysicsControlEditable.valueOf(control);
								
								final DefaultMutableTreeNode controlNode = new DefaultMutableTreeNode(edit);
								controlNode.add(new DefaultMutableTreeNode(edit.getShape()));
								selectedNode.add(controlNode);
								
								execute(new FinalMapping<>(
										new Runnable() {
											@Override
											public void run() {
												((SpatialEditable)obj).addControl(edit);
												selectedNode.add(controlNode);
												ui.getSceneGraphTreeModel().nodeStructureChanged(selectedNode);
											}
										},
										new Runnable() {
											@Override
											public void run() {
												((SpatialEditable)obj).removeControl(edit);
												selectedNode.remove(controlNode);
												ui.getSceneGraphTreeModel().nodeStructureChanged(selectedNode);
											}
										}
								));
							}
						}
					});
					addCollisionControl.setEnabled(isSpatial);
					addMenu.add(addCollisionControl);
					
					final JMenuItem removeMenu = new JMenuItem(new AbstractAction("Remove") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							if(JOptionPane.showConfirmDialog(null, "Really delete \"" + obj.toString() + "\"?", "", JOptionPane.YES_NO_OPTION) !=
									JOptionPane.YES_OPTION) {
								return;
							}
							
							if(obj instanceof AnimationControlMappingEditable) {
								final AnimationControlMappingEditable acme = (AnimationControlMappingEditable) obj;
								final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
								final AnimationControlEditable ace = (AnimationControlEditable) parentNode.getUserObject();
								final AnimationControl control = (AnimationControl) ace.getControl();
								
								final String internalName = acme.getInternalName();
								final String modelName = acme.getModelName();
								
								final Runnable refreshTreeRun = new Runnable() {
									@Override
									public void run() {
										rebuildAnimationControlPath(parentNode);
										ui.getSceneGraphTreeModel().nodeStructureChanged(selectedNode);
									}
								};
								execute(new FinalMapping<>(
										new Runnable() {
											@Override
											public void run() {
												control.removeActionMapping(internalName, modelName);
												EventQueue.invokeLater(refreshTreeRun);
											}
										},
										new Runnable() {
											@Override
											public void run() {
												control.addActionMapping(internalName, modelName);
												EventQueue.invokeLater(refreshTreeRun);
											}
										}
								));
							} else if(isSpatial) {
								final SpatialEditable se = (SpatialEditable) obj;
								final Spatial spatial = se.getSpatial();
								
								final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
								final NodeEditable pse = (NodeEditable) parentNode.getUserObject();
								final Node parentSpatial = pse.getSpatial();
								
								final int editableIndex = pse.getChildIndex(se);
								final int spatialIndex = parentSpatial.getChildIndex(spatial);
								final int nodeIndex = parentNode.getIndex(selectedNode);
								
								execute(new FinalMapping<>(
										new Runnable() {
											@Override
											public void run() {
												pse.removeChild(se);
												parentSpatial.detachChild(spatial);
												
												EventQueue.invokeLater(new Runnable() {
													@Override
													public void run() {
														selectedNode.removeFromParent();
														ui.getSceneGraphTreeModel().nodeStructureChanged(parentNode);
													}
												});
											}
										},
										new Runnable() {
											@Override
											public void run() {
												pse.addChild(se, editableIndex);
												parentSpatial.attachChildAt(spatial, spatialIndex);
												
												EventQueue.invokeLater(new Runnable() {
													@Override
													public void run() {
														parentNode.insert(selectedNode, nodeIndex);
														ui.getSceneGraphTreeModel().nodeStructureChanged(parentNode);
													}
												});
											}
										}
								));
							} else if(isControl) {
								final ControlEditable ce = (ControlEditable) obj;
								
								final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
								final SpatialEditable se = (SpatialEditable) parentNode.getUserObject();
								
								final int controlIndex = se.getControlIndex(ce);
								final int nodeIndex = parentNode.getIndex(selectedNode);
								
								execute(new FinalMapping<>(
										new Runnable() {
											@Override
											public void run() {
												se.removeControl(ce);
												
												EventQueue.invokeLater(new Runnable() {
													@Override
													public void run() {
														selectedNode.removeFromParent();
														ui.getSceneGraphTreeModel().nodeStructureChanged(parentNode);
													}
												});
											}
										},
										new Runnable() {
											@Override
											public void run() {
												se.addControl(ce, controlIndex);
												
												EventQueue.invokeLater(new Runnable() {
													@Override
													public void run() {
														parentNode.insert(selectedNode, nodeIndex);
														ui.getSceneGraphTreeModel().nodeStructureChanged(parentNode);
													}
												});
											}
										}
								));
							}
						}
					});
					removeMenu.setEnabled(obj instanceof AnimationControlMappingEditable ||
							(isSpatial && selectedNode.getParent() != null) ||
							(isControl && !(obj instanceof AnimationControlEditable) &&
									!(obj instanceof AnimControlEditable) &&
									!(obj instanceof AnimComposerEditable) &&
									!(((ControlEditable)obj).getControl() instanceof SkinningControl)));
					menu.add(removeMenu);
					
					menu.show(tree, e.getX(), e.getY());
				}
			}
		});
	}
	private void setupPropertiesWindow() {
		ui.getPropertiesWindow().setSize(setting.getPropertiesWindowSize());
		setWindowLocation(ui.getPropertiesWindow(), setting.getPropertiesWindowLocation());
		
		ui.getPropertiesWindow().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(final ComponentEvent e) {
				setting.setPropertiesWindowLocation(ui.getPropertiesWindow().getLocation());
			}
			@Override
			public void componentResized(final ComponentEvent e) {
				setting.setPropertiesWindowSize(ui.getPropertiesWindow().getSize());
			}
		});
		
		openedFile.addValueChangedHandler(new ValueChangedHandler<File>() {
			@Override
			public void valueChanged(final File oldValue, final File newValue) {
				ui.getPropertiesTable().setEnabled(newValue != null);
			}
		});
		
		
		final SetableMutableSingleValueModel<Editable> selectedObjectsTableObject = new SetableMutableSingleValueModelImpl<>();
		selectedObjectsEditorModes = new ArrayList<>();
		
		
		ui.getPropertiesTable().setModel(new DefaultTableModel() {
			private final String[] COLUMN_NAMES = new String[] {
					"Name",
					"Value"
			};
			
			@Override
			public void setValueAt(final Object aValue, final int row, final int column) {
				if(column == 1) {
					final Mapping<Runnable, Runnable> command = selectedObjectsEditorModes.get(row).createChangeCommand(selectedObjectsTableObject.source(), aValue);
					execute(command);
				}
			}
			@Override
			public Object getValueAt(final int row, final int column) {
				final EditionMode mode = selectedObjectsEditorModes.get(row);
				if(column == 0) {
					return(mode.getName());
				} else {
					return(mode.get());
				}
			}
			@Override
			public int getRowCount() {
				return(selectedObjectsTableObject.isSet() ? selectedObjectsEditorModes.size() : 0);
			}
			@Override
			public int getColumnCount() {
				return(2);
			}
			@Override
			public boolean isCellEditable(final int row, final int column) {
				return(column == 1);
			}
			@Override
			public Class<?> getColumnClass(final int columnIndex) {
				return(String.class);
			}
			@Override
			public String getColumnName(final int column) {
				return(COLUMN_NAMES[column]);
			}
		});
		
		selectionModel.addSelectionChangedListener(new SelectionListener<Editable>() {
			private final Deque<Editable> selections = new LinkedList<>();
			private Editable mainSelections;
			private final List<AbstractButton> buttons = new LinkedList<>();
			
			@Override
			public void selectionAdded(final Object source, final Editable value) {
				if(mainSelections != null) {
					selections.addFirst(mainSelections);
				}
				updateModes(value);
				mainSelections = value;
			}
			@Override
			public void selectionRemoved(final Object source, final Editable value) {
				if(value.equals(mainSelections)) {
					if(!selections.isEmpty()) {
						mainSelections = selections.removeFirst();
						updateModes(mainSelections);
					} else {
						mainSelections = null;
						
						selectedObjectsEditorModes.clear();
						ui.getPropertiesTable().revalidate();
						ui.getPropertiesTable().repaint();
						
						removeButtons();
					}
				} else {
					selections.remove(value);
				}
			}
			private void updateModes(final Editable value) {
				final Collection<EditionMode> modes = value.getEditionModes();
				selectedObjectsEditorModes.clear();
				removeButtons();
				
				JToggleButton buttonToSelect = null;
				final JToggleButton noEditModeButton = ui.getThreeDTransformNoneButton();
				int i = noEditModeButton.getParent().getComponentZOrder(noEditModeButton) + 1;
				for(final EditionMode mode:modes) {
					if(mode.isEditableByTable()) {
						selectedObjectsEditorModes.add(mode);
					}
					
					if(mode.isEditableByThreeDView()) {
						final JToggleButton button = new JToggleButton(mode.getName());
						button.addItemListener(new ItemListener() {
							@Override
							public void itemStateChanged(final ItemEvent e) {
								if(button.isSelected()) {
									app.enqueue(new Runnable() {
										@Override
										public void run() {
											final AppStateManager stateMng = app.getStateManager();
											final EditionState state = stateMng.getState(EditionState.class);
											if(state != null) {
												stateMng.detach(state);
											}
											
											stateMng.attach(mode.createEditionState(new EditionStateModel() {
												@Override
												public float getGridSize() {
													return(ui.getGridSizeSlider().getValue() / 100.0f);
												}
												
												@Override
												public GridMode getGridMode() {
													return(gridMode.source());
												}
												
												@Override
												public void execute(final Mapping<Runnable, Runnable> command) {
													EventQueue.invokeLater(new Runnable() {
														@Override
														public void run() {
															SwingUi.this.execute(command);
														}
													});
												}
												
												@Override
												public void executeTemporary(final Mapping<Runnable, Runnable> command) {
													command.getA().run();
													ui.getPropertiesTable().revalidate();
													ui.getPropertiesTable().repaint();
												}
											}));
										}
									});
									selectedMode = mode;
								}
							}
						});
						
						if(selectedMode == mode) {
							buttonToSelect = button;
						}
						
						ui.getThreeDTransformButtonGroup().add(button);
						ui.getThreeDTransformToolbar().add(button, i++);
						buttons.add(button);
					}
				}
				
				if(buttonToSelect != null) {
					buttonToSelect.setSelected(true);
				} else {
					noEditModeButton.setSelected(true);
				}
				
				selectedObjectsTableObject.sink(value);
				ui.getPropertiesTable().revalidate();
				ui.getPropertiesTable().repaint();
			}
			private void removeButtons() {
				for(final AbstractButton comp:buttons) {
					ui.getThreeDTransformToolbar().remove(comp);
					ui.getThreeDTransformButtonGroup().remove(comp);
				}
				buttons.clear();
				ui.getThreeDTransformToolbar().revalidate();
				ui.getThreeDTransformToolbar().repaint();
				ui.getThreeDTransformWindow().pack();
			}
		});
	}
	private void setupCameraWindow() {
		ui.getCameraWindow().setSize(setting.getCameraWindowSize());
		setWindowLocation(ui.getCameraWindow(), setting.getCameraWindowLocation());
		
		ui.getCameraWindow().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(final ComponentEvent e) {
				setting.setCameraWindowLocation(ui.getCameraWindow().getLocation());
			}
			@Override
			public void componentResized(final ComponentEvent e) {
				setting.setCameraWindowSize(ui.getCameraWindow().getSize());
			}
		});
		
		final Runnable setFlyByCamRunnable = new Runnable() {
			private final EditorCamera flyCamState = new FlyByCamEditorState();
			@Override
			public void run() {
				Utils.enqueueAndWait(app, new Runnable() {
					@Override
					public void run() {
						final EditorCamera existState = app.getStateManager().getState(EditorCamera.class);
						if(existState != null) {
							app.getStateManager().detach(existState);
						}
						app.getStateManager().attach(flyCamState);
					}
				});
			}
		};
		ui.getCameraFlyByCamButton().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				setFlyByCamRunnable.run();
			}
		});
		setFlyByCamRunnable.run();
		
		ui.getCameraChaseCamButton().addActionListener(new ActionListener() {
			private final EditorCamera state = new ChaseCameraEditorState();
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				Utils.enqueueAndWait(app, new Runnable() {
					@Override
					public void run() {
						final EditorCamera existState = app.getStateManager().getState(EditorCamera.class);
						if(existState != null) {
							app.getStateManager().detach(existState);
						}
						app.getStateManager().attach(state);
					}
				});
			}
		});
		
		ui.getCameraToOriginButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final EditorCamera existState = app.getStateManager().getState(EditorCamera.class);
				if(existState != null) {
					existState.reset();
				}
			}
		});
		ui.getCameraToSelectedButton().setEnabled(false);
		ui.getCameraToSelectedButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final TreePath path = ui.getSceneGraphTree().getSelectionPath();
				if(path == null) {
					return;
				}
				
				final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				final SpatialEditable selectedEditable = (SpatialEditable) selectedNode.getUserObject();
				
				
				final Vector3f vec = selectedEditable.getWorldTranslation();
				
				app.enqueue(new Runnable() {
					@Override
					public void run() {
						final EditorCamera cam = app.getStateManager().getState(EditorCamera.class);
						if(cam != null) {
							cam.move(vec);
						}
					}
				});
			}
		});
	}
	private void setupFilesWindow() {
		ui.getFilesWindow().setSize(setting.getFilesWindowSize());
		setWindowLocation(ui.getFilesWindow(), setting.getFilesWindowLocation());
		
		ui.getFilesWindow().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(final ComponentEvent e) {
				setting.setFilesWindowLocation(ui.getFilesWindow().getLocation());
			}
			@Override
			public void componentResized(final ComponentEvent e) {
				setting.setFilesWindowSize(ui.getFilesWindow().getSize());
			}
		});
		
		ui.getFilesTree().addFileOpenedListener(new DataSink<File>() {
			@Override
			public void sink(final File value) {
				requestOpenFile(value);
			}
		});
		
		ui.getFilesTree().addFolderChangedListener(new DataSink<DirectoryTree>() {
			@Override
			public void sink(final DirectoryTree value) {
				setting.setLastOpenedFolderPath(value.getCurrentDirectory().getAbsolutePath());
			}
		});
		
		final File file = new File(setting.getLastOpenedFolderPath());
		if(file.isDirectory()) {
			ui.getFilesTree().open(file);
		} else {
			ui.getFilesTree().open(new File("."));
		}
		
		final JPanel filesDialogPanel = new JPanel(new BorderLayout());
		filesDialogPanel.add(new JScrollPane(ui.getFilesTree().getTree(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		ui.getFilesWindow().add(filesDialogPanel, BorderLayout.CENTER);
	}
	private void setupThreeDTransform() {
		ui.getThreeDTransformWindow().setSize(setting.getThreeDTransformWindowSize());
		setWindowLocation(ui.getThreeDTransformWindow(), setting.getThreeDTransformWindowLocation());
		
		ui.getThreeDTransformWindow().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(final ComponentEvent e) {
				setting.setThreeDTransformWindowLocation(ui.getThreeDTransformWindow().getLocation());
			}
			@Override
			public void componentResized(final ComponentEvent e) {
				setting.setThreeDTransformWindowSize(ui.getThreeDTransformWindow().getSize());
			}
		});
		
		ui.getThreeDTransformNoneButton().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if(ui.getThreeDTransformNoneButton().isSelected()) {
					final AppStateManager stateMng = app.getStateManager();
					final EditionState state = stateMng.getState(EditionState.class);
					if(state != null) {
						stateMng.detach(state);
					}
				}
				selectedMode = null;
			}
		});
		selectionModel.addSelectionChangedListener(new SelectionListener<Editable>() {
			@Override
			public void selectionAdded(final Object source, final Editable value) {
				value.setSelected(true);
			}
			@Override
			public void selectionRemoved(final Object source, final Editable value) {
				value.setSelected(false);
			}
		});
	}
	
	private void requestExit() {
		if(openFileChanged.source()) {
			final int i = JOptionPane.showConfirmDialog(ui.getTopMenuWindow(), "File changed. Save before progress?", "", JOptionPane.YES_NO_CANCEL_OPTION);
			if(i == JOptionPane.CANCEL_OPTION) {
				return;
			} else if(i == JOptionPane.YES_OPTION) {
				saveFile();
				exit();
			} else {
				exit();
			}
		} else {
			exit();
		}
	}
	private void exit() {
		app.stop(true);
		Runtime.getRuntime().exit(0);
	}
	
	private void requestOpenFile(final File file) {
		if(openFileChanged.source()) {
			final int i = JOptionPane.showConfirmDialog(ui.getTopMenuWindow(), "File changed. Save before progress?", "", JOptionPane.YES_NO_CANCEL_OPTION);
			if(i == JOptionPane.CANCEL_OPTION) {
				return;
			} else if(i == JOptionPane.YES_OPTION) {
				saveFile();
				openFile(file);
			} else {
				openFile(file);
			}
		} else {
			openFile(file);
		}
	}
	private void openFile(final File file) {
		final String name = file.getName();
		final File parent = file.getParentFile();
		if(name.endsWith(".j3o")) {
			final String absolutePath = parent.getAbsolutePath();
			
			app.getAssetManager().clearCache();
			
			app.getAssetManager().registerLocator(absolutePath, FileLocator.class);
			try(final Reader in = new FileReader(file)) {
				final Spatial spatial = app.getAssetManager().loadModel(name);
				
				app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().removeAll(app.getRootNode());
				
				final SetableMutableSingleValueModel<SpatialEditable> editable = new SetableMutableSingleValueModelImpl<>();
				Utils.enqueueAndWait(app, new Runnable() {
					@Override
					public void run() {
						editable.sink(SpatialEditable.valueOf(app, spatial));
					}
				});
				tdview.clear();
				setObject(editable.source());
				
				openedFile.sink(file);
				openFileChanged.sink(Boolean.FALSE);
			} catch (final Throwable e) {
				JOptionPane.showMessageDialog(ui.getTopMenuWindow(), "Could not load file: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			} finally {
				app.getAssetManager().unregisterLocator(absolutePath, FileLocator.class);
			}
		} else if(name.contains(".")) {
			final String filenameBase = name.substring(0, name.lastIndexOf('.'));
			final String targetFilename = filenameBase + ".j3o";
			final File targetFile = new File(parent, targetFilename);
			
			if(targetFile.exists() &&
					JOptionPane.showConfirmDialog(null, targetFilename + " already exists. Overwrite?", "",
							JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				return;
			}
			
			try {
				final String absolutePath = parent.getAbsolutePath();
				
				app.getAssetManager().clearCache();
				app.getAssetManager().registerLocator(absolutePath, FileLocator.class);
				
				final Spatial spatial = app.getAssetManager().loadModel(name);
				
				app.getAssetManager().unregisterLocator(absolutePath, FileLocator.class);
				
				new BinaryExporter().save(spatial, targetFile);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	private void saveFile() {
		final DefaultTreeModel model = (DefaultTreeModel)ui.getSceneGraphTree().getModel();
		final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
		final SpatialEditable edit = (SpatialEditable) rootNode.getUserObject();
		
		Utils.enqueueAndWait(app, new Runnable() {
			@Override
			public void run() {
				final Spatial spatial = edit.createSpatial();
				
				try(OutputStream out = new FileOutputStream(openedFile.source())) {
					new BinaryExporter().save(spatial, out);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
				
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						openFileChanged.sink(Boolean.FALSE);
					}
				});
			}
		});
	}
	
	private void requestCloseFolder() {
		if(openFileChanged.source()) {
			final int i = JOptionPane.showConfirmDialog(ui.getTopMenuWindow(), "File changed. Save before progress?", "", JOptionPane.YES_NO_CANCEL_OPTION);
			if(i == JOptionPane.CANCEL_OPTION) {
				return;
			} else if(i == JOptionPane.YES_OPTION) {
				saveFile();
				closeFolder();
			} else {
				closeFolder();
			}
		} else {
			closeFolder();
		}
	}
	private void closeFolder() {
		openedFile.unset();
		
		fileSelectionTreeModel.setRoot(null);
		final DefaultTreeModel model = (DefaultTreeModel) ui.getSceneGraphTree().getModel();
		model.setRoot(null);
		
		tdview.clear();
		
		openFileChanged.sink(Boolean.FALSE);
		openedFolder.unset();
	}
	
	public void execute(final Mapping<Runnable, Runnable> command) {
		Utils.enqueueAndWait(app, new Runnable() {
			@Override
			public void run() {
				command.getA().run();
			}
		});
		undos.addFirst(command);
		redos.clear();
		openFileChanged.sink(Boolean.TRUE);
		
		ui.getPropertiesTable().revalidate();
		ui.getPropertiesTable().repaint();
	}
	public void undo() {
		if(undos.isEmpty()) {
			throw new NoSuchElementException("No undo available.");
		}
		
		final Mapping<Runnable, Runnable> mapping = undos.removeFirst();
		Utils.enqueueAndWait(app, new Runnable() {
			@Override
			public void run() {
				mapping.getB().run();
			}
		});
		redos.addFirst(mapping);
		openFileChanged.sink(Boolean.TRUE);
		
		ui.getPropertiesTable().revalidate();
		ui.getPropertiesTable().repaint();
	}
	public void redo() {
		if(redos.isEmpty()) {
			throw new NoSuchElementException("No redo available.");
		}
		
		final Mapping<Runnable, Runnable> mapping = redos.removeFirst();
		Utils.enqueueAndWait(app, new Runnable() {
			@Override
			public void run() {
				mapping.getA().run();
			}
		});
		undos.addFirst(mapping);
		openFileChanged.sink(Boolean.TRUE);
		
		ui.getPropertiesTable().revalidate();
		ui.getPropertiesTable().repaint();
	}
	
	private void rebuildAnimationControlPath(final DefaultMutableTreeNode node) {
		final Object obj = node.getUserObject();
		if(!(obj instanceof AnimationControlEditable)) {
			throw new IllegalArgumentException("rebuildAnimationControlPath requires an AnimationControlEditable node");
		}
		final AnimationControlEditable ace = (AnimationControlEditable) obj;
		final AnimationControl ac = (AnimationControl) ace.getControl();
		
		node.removeAllChildren();
		
		final List<Entry<String, String>> entries = new ArrayList<>(ac.getActionMappings().entrySet());
		Collections.sort(entries, new StringEntryKeySorter());
		
		for(final Entry<String, String> entry:entries) {
			node.add(new DefaultMutableTreeNode(new AnimationControlMappingEditable(entry.getKey(), entry.getValue())));
		}
	}
	
	private void addPhysicsControlNode(final PhysicsControlEditable edit, final DefaultMutableTreeNode parent) {
		final DefaultMutableTreeNode controlNode = new DefaultMutableTreeNode(edit);
		controlNode.add(new DefaultMutableTreeNode(edit.getShape()));
		parent.add(controlNode);
		
		ui.getSceneGraphTreeModel().nodeStructureChanged(parent);
	}
	
	public JFrame getFrame() {
		return (ui.getTopMenuWindow());
	}
	
	private void setObject(final SpatialEditable obj) {
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(obj);
		
		if(obj instanceof NodeEditable) {
			final NodeEditable asNode = (NodeEditable) obj;
			for(int i = 0, size = asNode.getChildCount(); i < size; i++) {
				final SpatialEditable child = asNode.getChild(i);
				setObject0(child, rootNode);
			}
			setObjectControls(asNode, rootNode);
		} else if(obj instanceof GeometryEditable) {
			final GeometryEditable asGeo = (GeometryEditable)obj;
			rootNode.add(new DefaultMutableTreeNode(asGeo.getMesh()));
			setObjectControls(asGeo, rootNode);
		}
		
		((DefaultTreeModel)ui.getSceneGraphTree().getModel()).setRoot(rootNode);
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				tdview.setObject(obj);
			}
		});
	}
	private void setObject0(final SpatialEditable obj, final DefaultMutableTreeNode parent) {
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj);
		parent.add(node);
		
		if(obj instanceof NodeEditable) {
			final NodeEditable asNode = (NodeEditable) obj;
			for(int i = 0, size = asNode.getChildCount(); i < size; i++) {
				final SpatialEditable child = asNode.getChild(i);
				setObject0(child, node);
			}
			setObjectControls(asNode, parent);
		} else if(obj instanceof GeometryEditable) {
			final GeometryEditable asGeo = (GeometryEditable)obj;
			node.add(new DefaultMutableTreeNode(asGeo.getMesh()));
			setObjectControls(asGeo, node);
		}
	}
	private void setObjectControls(final SpatialEditable obj, final DefaultMutableTreeNode parent) {
		for(final ControlEditable control:obj.getControls()) {
			final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(control);
			parent.add(newNode);
			
			if(control instanceof AnimComposerEditable) {
				final AnimComposerEditable asAnimComposerEditable = (AnimComposerEditable) control;
				for(final String s:asAnimComposerEditable.getAnimationNames()) {
					newNode.add(new DefaultMutableTreeNode(new AnimComposerActionEditable(s, asAnimComposerEditable)));
				}
			} else if(control instanceof AnimControlEditable) {
				final AnimControlEditable asAnimControlEditable = (AnimControlEditable) control;
				for(final String s:asAnimControlEditable.getAnimationNames()) {
					newNode.add(new DefaultMutableTreeNode(new AnimControlActionEditable(s, asAnimControlEditable)));
				}
			} else if(control instanceof SkinningControlEditable) {
				final SkinningControlEditable asSkinningEditable = (SkinningControlEditable) control;
				for(final Joint j:asSkinningEditable.getJointList()) {
					newNode.add(new DefaultMutableTreeNode(SpatialEditable.valueOf(app, asSkinningEditable.getAttachmentsNode(j.getName()))));
				}
			} else if(control instanceof PhysicsControlEditable) {
				final CollisionShapeEditable shape = ((PhysicsControlEditable) control).getShape();
				newNode.add(new DefaultMutableTreeNode(shape));
			}
		}
	}
	
	private void addObject(final SpatialEditable obj, final DefaultMutableTreeNode parent) {
		((NodeEditable) parent.getUserObject()).addChild(obj);
		parent.add(addObject0(obj));
		((DefaultTreeModel)ui.getSceneGraphTree().getModel()).nodeStructureChanged(parent);
		tdview.addObject(obj, (NodeEditable)parent.getUserObject());
	}
	private DefaultMutableTreeNode addObject0(final Editable obj) {
		final DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(obj);
		
		if(obj instanceof NodeEditable) {
			final NodeEditable asNode = (NodeEditable) obj;
			for(int i = 0, size = asNode.getChildCount(); i < size; i++) {
				final SpatialEditable child = asNode.getChild(i);
				treeNode.add(addObject0(child));
				asNode.addChild(child);
			}
		} else if(obj instanceof GeometryEditable) {
			treeNode.add(new DefaultMutableTreeNode(((GeometryEditable)obj).getMesh()));
		}
		return(treeNode);
	}
	
	private void removeObject(final SpatialEditable obj, final DefaultMutableTreeNode parent) {
		final NodeEditable parentObject = (NodeEditable) parent.getUserObject();
		
		for(int i = 0, size = parent.getChildCount(); i < size; i++) {
			final DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
			if(child.getUserObject() == obj) {
				parent.remove(i);
				parentObject.removeChild(obj);
				
				((DefaultTreeModel)ui.getSceneGraphTree().getModel()).nodeStructureChanged(parent);
				//tdview.addObject(obj, parentObject);
				break;
			}
		}
	}
	
	private void createEditable(final SpatialEditable editable, final DefaultMutableTreeNode parent) {
		execute(new FinalMapping<>(new Runnable() {
			@Override
			public void run() {
				addObject(editable, parent);
			}
		}, new Runnable() {
			@Override
			public void run() {
				removeObject(editable, parent);
			}
		}));
	}
	
	public void show() {
		for(final Window window:getAllWindows()) {
			window.setVisible(true);
		}
	}
	public void show(final Window window) {
		window.setVisible(true);
	}
	private void rearrangeWindows() {
		final int screenWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
		final int screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
		
		final Dimension scaledscreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//final double scaleFactorWidth = screenWidth / (double)scaledscreenSize.width;
		//final double scaleFactorHeight = screenHeight / (double)scaledscreenSize.height;
		
		final int[] threeDViewWidth = new int[1];
		final int[] threeDViewHeight = new int[1];
		
		final long windowHandle = ((LwjglDisplay)app.getContext()).getWindowHandle();
		GLFW.glfwGetWindowSize(windowHandle, threeDViewWidth, threeDViewHeight);
		
		GLFW.glfwSetWindowPos(windowHandle, screenWidth / 2 - threeDViewWidth[0] / 2, screenHeight / 2 - threeDViewHeight[0] / 2);
		
		final JFrame toolbarWindow = ui.getTopMenuWindow();
		toolbarWindow.pack();
		setWindowLocation(toolbarWindow,
				new Point(
						scaledscreenSize.width / 2 - toolbarWindow.getWidth() / 2,
						scaledscreenSize.height / 2 - toolbarWindow.getHeight() / 2 - threeDViewHeight[0] / 2)
		);
		
		final JDialog filesWindow = ui.getFilesWindow();
		filesWindow.pack();
		setWindowLocation(filesWindow,
				new Point(
						scaledscreenSize.width / 2 - filesWindow.getWidth() / 2 - threeDViewWidth[0] / 2,
						scaledscreenSize.height / 2 - filesWindow.getHeight())
				);
		
		final JDialog sceneGraphWindow = ui.getSceneGraphWindow();
		sceneGraphWindow.pack();
		setWindowLocation(sceneGraphWindow,
				new Point(
						scaledscreenSize.width / 2 - sceneGraphWindow.getWidth() / 2 - threeDViewWidth[0] / 2,
						scaledscreenSize.height / 2
		));
		
		final JDialog propertyWindow = ui.getPropertiesWindow();
		propertyWindow.pack();
		setWindowLocation(propertyWindow,
				new Point(
						scaledscreenSize.width / 2 - propertyWindow.getWidth() / 2 + threeDViewWidth[0] / 2,
						scaledscreenSize.height / 2 - propertyWindow.getHeight()
		));
		
		final JDialog cameraWindow = ui.getCameraWindow();
		cameraWindow.pack();
		setWindowLocation(cameraWindow,
				new Point(
						scaledscreenSize.width / 2 - cameraWindow.getWidth() / 2 + threeDViewWidth[0] / 2,
						scaledscreenSize.height / 2
		));
		
		final JDialog threeDTransformWindow = ui.getThreeDTransformWindow();
		threeDTransformWindow.pack();
		setWindowLocation(threeDTransformWindow,
				new Point(
						scaledscreenSize.width / 2 - threeDTransformWindow.getWidth() / 2,
						scaledscreenSize.height / 2 - threeDTransformWindow.getHeight() / 2 + threeDViewHeight[0] / 2
		));
	}
	private void setWindowLocation(final Window window, final Point location) {
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		if(location.x < 0) {
			location.x = 0;
		}
		if(location.y < 0) {
			location.y = 0;
		}
		if(location.x + window.getWidth() >= screenSize.width) {
			location.x = screenSize.width - ((int)(window.getWidth()));
		}
		if(location.y + window.getHeight() >= screenSize.height) {
			location.y = screenSize.height - ((int)(window.getHeight()));
		}
		
		window.setLocation(location);
	}
	private Window[] getAllWindows() {
		return(new Window[] {
			ui.getTopMenuWindow(),
			ui.getSceneGraphWindow(),
			ui.getPropertiesWindow(),
			ui.getFilesWindow(),
			ui.getCameraWindow(),
			ui.getThreeDTransformWindow()
		});
	}
}
