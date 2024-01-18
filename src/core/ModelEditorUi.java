package core;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
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
import core.editables.BoxMeshEditable;
import core.editables.CylinderMeshEditable;
import core.editables.DomeMeshEditable;
import core.editables.Editable;
import core.editables.EditionMode;
import core.editables.GeometryEditable;
import core.editables.MeshEditable;
import core.editables.NodeEditable;
import core.editables.SpatialEditable;
import core.editables.SphereMeshEditable;
import core.editables.TorusMeshEditable;
import core.setting.ModelSetting;
import core.threed.EditionState;
import core.threed.EditionStateModel;
import core.ui.ActualTypeTable;
import core.ui.FloatTableEditor;
import core.ui.IntegerTableEditor;
import core.ui.QuaternionTableEditor;
import core.ui.QuaternionTableRenderer;
import core.ui.UiElementsStruct;
import core.ui.Vector3fTableEditor;
import core.ui.Vector3fTableRenderer;
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

public final class ModelEditorUi {
	private final JFrame frame;
	
	private final SetableMutableSingleValueModel<File> openedFolder = new SetableMutableSingleValueModelImpl<>();
	private final SetableMutableSingleValueModel<File> openedFile = new SetableMutableSingleValueModelImpl<>();
	
	private final MutableSingleValueModel<Boolean> openFileChanged = new MutableSingleValueModelImpl<>(Boolean.FALSE);
	
	private final MutableDequeModel<Mapping<Runnable, Runnable>> undos = new MutableDequeModelImpl<>();
	private final MutableDequeModel<Mapping<Runnable, Runnable>> redos = new MutableDequeModelImpl<>();
	
	private DefaultTreeModel fileSelectionTreeModel;
	private JTree fileSelectionTree;
	private final JTree componentTree;
	
	private final ThreeDimensionalView tdview;
	
	private final List<EditionMode> selectedObjectsEditorModes;
	
	private EditionMode selectedMode;
	
	private final ModelSetting setting;
	private final SimpleApplication app;
	
	private JTable valuesTable;
	
	private UiElementsStruct ui;
	
	public ModelEditorUi(final SimpleApplication app, final ModelSetting setting) {
		this.app = Requires.notNull(app, "app == null");
		this.setting = Requires.notNull(setting, "setting == null");
		
		ui = new UiElementsStruct();
		
		frame = ui.getFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				requestExit();
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
		
		
		final MutableSingleValueModel<GridMode> gridMode = new MutableSingleValueModelImpl<>(GridMode.NONE);
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
		
		
		//
		
		final SelectionModel<Editable> selectionModel = new SelectionModelImpl<>();
		
		tdview = new ThreeDimensionalView(app);
		tdview.setSelectionModel(selectionModel);
		
		
		//
		
		setUpFilesDialog();
		
		//
		
		final TreeModel componentTreeModel = new DefaultTreeModel(null) {
			@Override
			public void valueForPathChanged(final TreePath path, final Object newValue) {
				System.out.println(newValue);
				super.valueForPathChanged(path, newValue);
			}
		};
		componentTree = new JTree(componentTreeModel);
		componentTree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded,
					final boolean leaf, final int row, final boolean hasFocus) {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				final Editable editable = (Editable) node.getUserObject();
				return(super.getTreeCellRendererComponent(fileSelectionTree, value, sel, expanded, editable != null ? !(editable instanceof core.editables.NodeEditable) : leaf, row, hasFocus));
			}
		});
		componentTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				final boolean b = (!componentTree.isSelectionEmpty());
				//moveCameraToSelectedButton.setEnabled(b);
			}
		});
		ui.getSceneGraphDialog().add(new JScrollPane(componentTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		//final Component threeDimView = tdview.getComponent();
		
		//threeDimView.setMinimumSize(new Dimension(640, 480));
		//threeDimView.setPreferredSize(new Dimension(640, 480));
		
		final Canvas canvas = new Canvas();
		canvas.setBackground(Color.BLUE);
		
		
		final SetableMutableSingleValueModel<Editable> selectedObjectsTableObject = new SetableMutableSingleValueModelImpl<>();
		selectedObjectsEditorModes = new ArrayList<>();
		
		
		valuesTable = new ActualTypeTable();
		valuesTable.setModel(new DefaultTableModel() {
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
		valuesTable.setDefaultEditor(Float.class, new FloatTableEditor());
		valuesTable.setDefaultEditor(Integer.class, new IntegerTableEditor());
		valuesTable.setDefaultEditor(Boolean.class, new DefaultCellEditor(new JCheckBox()) {
			@Override
			public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row,
					final int column) {
				final JCheckBox comp = (JCheckBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
				comp.setSelected(Boolean.parseBoolean(Objects.toString(value)));
				return(comp);
			}
		});
		valuesTable.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
					final boolean hasFocus, final int row, final int column) {
				final JCheckBox box = new JCheckBox();
				box.setSelected(Boolean.parseBoolean(Objects.toString(value)));
				return(box);
			}
		});
		
		valuesTable.setDefaultRenderer(Vector3f.class, new Vector3fTableRenderer());
		valuesTable.setDefaultEditor(Vector3f.class, new Vector3fTableEditor());
		valuesTable.setDefaultRenderer(Quaternion.class, new QuaternionTableRenderer());
		valuesTable.setDefaultEditor(Quaternion.class, new QuaternionTableEditor());
		
		openedFile.addValueChangedHandler(new ValueChangedHandler<File>() {
			@Override
			public void valueChanged(final File oldValue, final File newValue) {
				valuesTable.setEnabled(newValue != null);
			}
		});
		valuesTable.getTableHeader().setReorderingAllowed(false);
		valuesTable.setEnabled(false);
		ui.getPropertiesDialog().add(new JScrollPane(valuesTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		//frame.add(vSplit);
		
		
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
						valuesTable.revalidate();
						valuesTable.repaint();
						
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
															ModelEditorUi.this.execute(command);
														}
													});
												}
												
												@Override
												public void executeTemporary(final Mapping<Runnable, Runnable> command) {
													command.getA().run();
													valuesTable.revalidate();
													valuesTable.repaint();
												}
											}));
											
											/*stateMng.attach(new TranslationState(new EditionStateModel() {
												@Override
												public void set(final String data) {
													final Mapping<Runnable, Runnable> command = mode.createChangeCommand(value, data);
													execute(command);
													
													valuesTable.revalidate();
													valuesTable.repaint();
												}
												@Override
												public void setTemporary(final String data) {
													mode.createChangeCommand(value, data).getA().run();
													valuesTable.revalidate();
													valuesTable.repaint();
												}
												@Override
												public String get() {
													throw new UnsupportedOperationException();
												}
												@Override
												public Vector3f getCursorLocation() {
													return(EditionModeOld.strToVec(String.valueOf(EditionModeOld.WORLD_TRANSLATION.get(value))));
												}
												@Override
												public float getGridSize() {
													return(gridSizeSlider.getValue() / 100.0f);
												}
												@Override
												public GridMode getGridMode() {
													return(gridMode.source());
												}
											}));*/
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
				valuesTable.revalidate();
				valuesTable.repaint();
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
		
		
		//
		
		final MutableSingleValueModel<Boolean> updateTreeSelection = new MutableSingleValueModelImpl<>(Boolean.TRUE);
		
		final SelectionListener<Editable> updateComponentTreeSelectionListener = new SelectionListener<Editable>() {
			@Override
			public void selectionAdded(final Object source, final Editable value) {
				value.setSelected(true);
				
				if(updateTreeSelection.source()) {
					final DefaultMutableTreeNode node = searchTreeNodeForUserObject((DefaultMutableTreeNode) componentTree.getModel().getRoot(), value);
					
					if(node != null) {
						final TreeNode[] nodes = ((DefaultTreeModel)componentTree.getModel()).getPathToRoot(node);
						final TreePath[] curPaths = componentTree.getSelectionPaths();
						if(curPaths == null) {
							componentTree.setSelectionPath(new TreePath(nodes));
						} else {
							final int len = curPaths.length;
							
							final TreePath[] newPaths = new TreePath[len + 1];
							System.arraycopy(curPaths, 0, newPaths, 0, len);
							newPaths[len] = new TreePath(nodes);
							componentTree.setSelectionPaths(newPaths);
						}
					}
				}
			}
			@Override
			public void selectionRemoved(final Object source, final Editable value) {
				value.setSelected(false);
				
				if(updateTreeSelection.source()) {
					final DefaultMutableTreeNode node = searchTreeNodeForUserObject((DefaultMutableTreeNode) componentTree.getModel().getRoot(), value);
					
					if(node != null) {
						final TreeNode[] nodes = ((DefaultTreeModel)componentTree.getModel()).getPathToRoot(node);
						final TreePath[] curPaths = componentTree.getSelectionPaths();
						if(curPaths == null) {
							return;
						} else {
							final Collection<TreePath> curPathCol = new ArrayList<>(Arrays.asList(curPaths));
							curPathCol.remove(new TreePath(nodes));
							componentTree.setSelectionPaths(curPathCol.toArray(new TreePath[curPathCol.size()]));
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
		
		componentTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				updateTreeSelection.sink(Boolean.FALSE);
				
				selectionModel.clear();
				
				final TreePath[] paths = componentTree.getSelectionPaths();
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
		
		componentTree.setEditable(true);
		
		
		//
		
		componentTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					final TreePath path = componentTree.getPathForLocation(e.getX(), e.getY());
					if(path == null) {
						return;
					}
					
					componentTree.setSelectionPath(path);
					
					final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
					final Editable obj = (Editable) selectedNode.getUserObject();
					
					
					final JPopupMenu menu = new JPopupMenu();
					
					final JMenu addMenu = new JMenu("Add");
					addMenu.setEnabled(obj instanceof NodeEditable);
					menu.add(addMenu);
					
					addMenu.add(new JMenuItem(new AbstractAction("Node") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final NodeEditable newNode = new NodeEditable(app, new Node(""));
							newNode.setName("New node");
							createEditable(newNode, selectedNode);
						}
					}));
					/*addMenu.add(new JMenuItem(new AbstractAction("AudioNode") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							/*final AudioNodeEditable newNode = new AudioNodeEditable(app, "New audio node");
							createEditable(newNode, selectedNode);
							openFileChanged.sink(Boolean.TRUE);*/
						/*}
					}));*/
					
					addMenu.addSeparator();
					
					addMenu.add(new JMenuItem(new AbstractAction("Import model") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final JFileChooser chooser = new JFileChooser(setting.getLastLoadedModelPath());
							if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
								
								final Path openPath = openedFolder.source().toPath();
								final File selectedFile = chooser.getSelectedFile();
								final Path selectedPath = selectedFile.toPath();
								setting.setLastLoadedModelPath(selectedFile.getAbsolutePath());
								
								final File relativePath = new File(openPath.relativize(selectedPath).toString());
								if(relativePath.getPath().replace('\\', '/').matches("(?:^|/)[.][.](?:$|/)")) {
									// TODO import
									System.out.println("Ask for import");
								}
								
								
								final String path = selectedFile.getPath();
								final String parentPath = selectedFile.getParentFile().getPath();
								
								app.getAssetManager().registerLocator(parentPath, FileLocator.class);
								
								final Spatial spatial = app.getAssetManager().loadModel(selectedFile.getName());
								createEditable(SpatialEditable.valueOf(app, spatial), selectedNode);
								
								app.getAssetManager().unregisterLocator(parentPath, FileLocator.class);
								
								openFileChanged.sink(Boolean.TRUE);
							}
						}
					}));
					menu.show(componentTree, e.getX(), e.getY());
					
					addMenu.addSeparator();
					
					addMenu.add(new JMenuItem(new AbstractAction("Box") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Box box = new Box(0.5f, 0.5f, 0.5f);
							final BoxMeshEditable boxEditable = new BoxMeshEditable(box);
							
							final Geometry geo = new Geometry("New box", boxEditable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, boxEditable);
							
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					addMenu.add(new JMenuItem(new AbstractAction("Cylinder") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Cylinder cylinder = new Cylinder(8, 32, 0.5f, 1, true, false);
							final MeshEditable editable = new CylinderMeshEditable(cylinder);
							
							final Geometry geo = new Geometry("New cylinder", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					addMenu.add(new JMenuItem(new AbstractAction("Sphere") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Sphere sphere = new Sphere(16, 32, 1);
							final MeshEditable editable = new SphereMeshEditable(sphere);
							
							final Geometry geo = new Geometry("New sphere", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					addMenu.add(new JMenuItem(new AbstractAction("Dome") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Dome dome = new Dome(8, 16, 1);
							final MeshEditable editable = new DomeMeshEditable(dome);
							
							final Geometry geo = new Geometry("New dome", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
					addMenu.add(new JMenuItem(new AbstractAction("Cone") {
						@Override
						public void actionPerformed(final ActionEvent e) {
							final Dome dome = new Dome(2, 16, 1);
							final MeshEditable editable = new DomeMeshEditable(dome);
							
							final Geometry geo = new Geometry("New cone", editable.getMesh());
							
							final Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
							mat.setColor("Color", ColorRGBA.randomColor());
							geo.setMaterial(mat);
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, editable);
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
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, editable);
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
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, editable);
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
							
							final GeometryEditable geoEdit = new GeometryEditable(app, geo, editable);
							createEditable(geoEdit, selectedNode);
							openFileChanged.sink(Boolean.TRUE);
						}
					}));
				}
			}
		});
		
		
		setupTopMenuWindow();
		setupCameraWindows();
	}
	
	private void setupTopMenuWindow() {
		ui.getTopMenuFileOpen().addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(final ActionEvent e) {
				showLoadFolderUi();
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
		
		
		
		ui.getToolbarOpen().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				showLoadFolderUi();
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
	}
	private void setupCameraWindows() {
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
				final TreePath path = componentTree.getSelectionPath();
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
		componentTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				final TreePath path = componentTree.getSelectionPath();
				if(path == null) {
					return;
				}
				final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				ui.getCameraToSelectedButton().setEnabled(selectedNode.getUserObject() instanceof SpatialEditable);
			}
		});
	}
	
	private void setUpFilesDialog() {
		final JPanel filesDialogPanel = new JPanel(new BorderLayout());
		
		final JPanel createFilesPanel = new JPanel();
		filesDialogPanel.add(createFilesPanel, BorderLayout.NORTH);
		
		final JButton createFilesModelFileButton = new JButton(new AbstractAction("New model File into root") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final File newFilePath = getFreeFileName(openedFolder.source(), "new", ".j3o");
				try(final OutputStream out = new FileOutputStream(newFilePath)) {
					new BinaryExporter().save(new Node("New Model"), out);
					
					final DefaultMutableTreeNode root = (DefaultMutableTreeNode) fileSelectionTreeModel.getRoot();
					
					final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(newFilePath));
					root.add(newNode);
					fileSelectionTreeModel.nodeStructureChanged(root);
					
					final TreePath newPath = new TreePath(new DefaultMutableTreeNode[] { root, newNode });
					fileSelectionTree.scrollPathToVisible(newPath);
					fileSelectionTree.startEditingAtPath(newPath);
				} catch (final Throwable e1) {
					JOptionPane.showMessageDialog(frame, "File creation failed: " + e1.getMessage());
				}
			}
		});
		createFilesModelFileButton.setEnabled(false);
		openedFolder.addValueChangedHandler(new ValueChangedHandler<File>() {
			@Override
			public void valueChanged(final File oldValue, final File newValue) {
				createFilesModelFileButton.setEnabled(newValue != null);
			}
		});
		createFilesPanel.add(createFilesModelFileButton);
		
		final JButton createFilesFileButton = new JButton(new AbstractAction("New text File into root") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final File newFilePath = getFreeFileName(openedFolder.source(), "new", "");
				try {
					if(!newFilePath.createNewFile()) {
						JOptionPane.showMessageDialog(frame, "File creation failed.");
					}
					
					final DefaultMutableTreeNode root = (DefaultMutableTreeNode) fileSelectionTreeModel.getRoot();
					
					final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(newFilePath));
					root.add(newNode);
					fileSelectionTreeModel.nodeStructureChanged(root);
					
					final TreePath newPath = new TreePath(new DefaultMutableTreeNode[] { root, newNode });
					fileSelectionTree.scrollPathToVisible(newPath);
					fileSelectionTree.startEditingAtPath(newPath);
				} catch (final IOException e1) {
					JOptionPane.showMessageDialog(frame, "File creation failed: " + e1.getMessage());
				}
			}
		});
		createFilesFileButton.setEnabled(false);
		openedFolder.addValueChangedHandler(new ValueChangedHandler<File>() {
			@Override
			public void valueChanged(final File oldValue, final File newValue) {
				createFilesFileButton.setEnabled(newValue != null);
			}
		});
		createFilesPanel.add(createFilesFileButton);
		
		
		final JButton createFilesDirectoryButton = new JButton(new AbstractAction("New Directory into root") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final File newDirectoryPath = getFreeFileName(openedFolder.source(), "new", "");
				if(!newDirectoryPath.mkdir()) {
					JOptionPane.showMessageDialog(frame, "File creation failed.");
				}
				
				final DefaultMutableTreeNode root = (DefaultMutableTreeNode) fileSelectionTreeModel.getRoot();
				
				final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(newDirectoryPath));
				root.add(newNode);
				fileSelectionTreeModel.nodeStructureChanged(root);
				
				final TreePath newPath = new TreePath(new DefaultMutableTreeNode[] { root, newNode });
				fileSelectionTree.scrollPathToVisible(newPath);
				fileSelectionTree.startEditingAtPath(newPath);
			}
		});
		createFilesDirectoryButton.setEnabled(false);
		openedFolder.addValueChangedHandler(new ValueChangedHandler<File>() {
			@Override
			public void valueChanged(final File oldValue, final File newValue) {
				createFilesDirectoryButton.setEnabled(newValue != null);
			}
		});
		createFilesPanel.add(createFilesDirectoryButton);
		
		fileSelectionTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode()) {
			@Override
			public void valueForPathChanged(final TreePath path, final Object newValue) {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				final File curFile = ((FileTreeNodeWrap) node.getUserObject()).getFile();
				final File newFile = new File(curFile.getParentFile(), Objects.toString(newValue));
				
				if(newFile.exists()) {
					JOptionPane.showMessageDialog(frame, "Target path already exists", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				final boolean b = curFile.renameTo(newFile);
				if(!b) {
					JOptionPane.showMessageDialog(frame, "Renaming file failed", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				node.setUserObject(new FileTreeNodeWrap(newFile));
				
				nodeChanged(node);
			}
		};
		fileSelectionTree = new JTree(fileSelectionTreeModel);
		fileSelectionTree.setRootVisible(false);
		fileSelectionTree.setShowsRootHandles(true);
		fileSelectionTree.setEditable(true);
		fileSelectionTree.setExpandsSelectedPaths(true);
		fileSelectionTree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded,
					final boolean leaf, final int row, final boolean hasFocus) {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				final FileTreeNodeWrap wrap = (FileTreeNodeWrap) node.getUserObject();
				
				if(wrap != null) {
					final boolean isFolder = wrap.getFile().isDirectory();
					return(super.getTreeCellRendererComponent(tree, value, sel, expanded, !isFolder, row, hasFocus));
				} else {
					return(super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus));
				}
			}
		});
		fileSelectionTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if((e.getClickCount() & 1) == 0) {
					final TreePath path = fileSelectionTree.getSelectionPath();
					if(path != null) {
						final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						final File f = ((FileTreeNodeWrap)node.getUserObject()).getFile();
						if(f.isFile()) {
							requestOpenFile(f);
						}
					}
				}
			}
			@Override
			public void mousePressed(final MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					final TreePath path = fileSelectionTree.getPathForLocation(e.getX(), e.getY());
					if(path != null) {
						fileSelectionTree.setSelectionPath(path);
						
						final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						final File file = ((FileTreeNodeWrap) node.getUserObject()).getFile();
						
						final JPopupMenu menu = new JPopupMenu();
						
						if(file.isFile()) {
							menu.add(new AbstractAction("Open") {
								@Override
								public void actionPerformed(final ActionEvent e) {
									requestOpenFile(file);
								}
							});
							menu.addSeparator();
						}
						menu.add(new AbstractAction("Rename") {
							@Override
							public void actionPerformed(final ActionEvent e) {
								fileSelectionTree.startEditingAtPath(path);
							}
						});
						menu.add(new AbstractAction("Delete") {
							@Override
							public void actionPerformed(final ActionEvent e) {
								final int i = JOptionPane.showConfirmDialog(frame, "Realy delete file " + file.getName() + "?", "", JOptionPane.YES_NO_OPTION);
								if(i == JOptionPane.YES_OPTION) {
									if(!deleteFile(file)) {
										JOptionPane.showMessageDialog(frame, "File deletion failed");
									}
								}
								
								final TreeNode parent = node.getParent();
								node.removeFromParent();
								fileSelectionTreeModel.nodeStructureChanged(parent);
							}
							private boolean deleteFile(final File file) {
								if(!file.delete()) {
									return(false);
								}
								
								if(file.isDirectory()) {
									for(final File content:file.listFiles()) {
										if(!deleteFile(content)) {
											return(false);
										}
									}
								}
								return(true);
							}
						});
						if(file.isDirectory()) {
							menu.addSeparator();
							
							final JMenu newMenu = new JMenu("New");
							
							newMenu.add(new AbstractAction("Model File") {
								@Override
								public void actionPerformed(final ActionEvent e) {
									final File newFilePath = getFreeFileName(file, "new", ".j3o");
									try {
										if(!newFilePath.createNewFile()) {
											JOptionPane.showMessageDialog(frame, "File creation failed.");
										}
										
										final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(newFilePath));
										node.add(newNode);
										fileSelectionTreeModel.nodeStructureChanged(node);
										
										final TreePath newPath = new TreePath(new DefaultMutableTreeNode[] { node, newNode });
										fileSelectionTree.scrollPathToVisible(newPath);
										fileSelectionTree.startEditingAtPath(newPath);
									} catch (final IOException e1) {
										JOptionPane.showMessageDialog(frame, "File creation failed: " + e1.getMessage());
									}
								}
							});
							newMenu.add(new AbstractAction("Empty File") {
								@Override
								public void actionPerformed(final ActionEvent e) {
									final File newFilePath = getFreeFileName(file, "new", "");
									try {
										if(!newFilePath.createNewFile()) {
											JOptionPane.showMessageDialog(frame, "File creation failed.");
										}
										
										final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(newFilePath));
										node.add(newNode);
										fileSelectionTreeModel.nodeStructureChanged(node);
										
										final TreePath newPath = new TreePath(new DefaultMutableTreeNode[] { node, newNode });
										fileSelectionTree.scrollPathToVisible(newPath);
										fileSelectionTree.startEditingAtPath(newPath);
									} catch (final IOException e1) {
										JOptionPane.showMessageDialog(frame, "File creation failed: " + e1.getMessage());
									}
								}
							});
							newMenu.add(new AbstractAction("Directory") {
								@Override
								public void actionPerformed(final ActionEvent e) {
									final File newDirectoryPath = getFreeFileName(file, "new", "");
									if(!newDirectoryPath.mkdir()) {
										JOptionPane.showMessageDialog(frame, "File creation failed.");
									}
									
									final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(newDirectoryPath));
									node.add(newNode);
									fileSelectionTreeModel.nodeStructureChanged(node);
									
									final TreePath newPath = new TreePath(new DefaultMutableTreeNode[] { node, newNode });
									fileSelectionTree.scrollPathToVisible(newPath);
									fileSelectionTree.startEditingAtPath(newPath);
								}
							});
							menu.add(newMenu);
						}
						
						menu.show(fileSelectionTree, e.getX(), e.getY());
					}
				}
			}
		});
		filesDialogPanel.add(new JScrollPane(fileSelectionTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		ui.getFilesDialog().add(filesDialogPanel, BorderLayout.CENTER);
	}
	private void showLoadFolderUi() {
		final JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return("Directory");
			}
			@Override
			public boolean accept(final File f) {
				return(f.isDirectory());
			}
		});
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setSelectedFile(new File(setting.getLastOpenedFolderPath()));
		final int i = chooser.showOpenDialog(frame);
		if(i == JFileChooser.APPROVE_OPTION) {
			final File f = chooser.getSelectedFile();
			loadFolder(f);
			setting.setLastOpenedFolderPath(f.getAbsolutePath());
			app.getAssetManager().registerLocator(f.getAbsolutePath(), FileLocator.class);
		}
	}
	private void loadFolder(final File f) {
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(f));
		
		final File[] content = f.listFiles();
		Arrays.sort(content, new Comparator<File>() {
			@Override
			public int compare(final File o1, final File o2) {
				final boolean b0 = o1.isDirectory();
				final boolean b1 = o2.isDirectory();
				
				if(!b0 && b1) {
					return(1);
				} else if(b0 && !b1) {
					return(-1);
				} else {
					return(o1.getName().compareTo(o2.getName()));
				}
			}
		});
		for(final File child:content) {
			loadFolder0(child, rootNode);
		}
		
		fileSelectionTreeModel.setRoot(rootNode);
		openedFolder.sink(f);
	}
	private void loadFolder0(final File f, final DefaultMutableTreeNode parentNode) {
		final DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode(new FileTreeNodeWrap(f));
		parentNode.add(thisNode);
		
		if(f.isDirectory()) {
			final File[] content = f.listFiles();
			Arrays.sort(content, new Comparator<File>() {
				@Override
				public int compare(final File o1, final File o2) {
					final boolean b0 = o1.isDirectory();
					final boolean b1 = o2.isDirectory();
					
					if(!b0 && b1) {
						return(1);
					} else if(b0 && !b1) {
						return(-1);
					} else {
						return(o1.getName().compareTo(o2.getName()));
					}
				}
			});
			for(final File child:content) {
				loadFolder0(child, thisNode);
			}
		}
	}
	
	private void requestExit() {
		if(openFileChanged.source()) {
			final int i = JOptionPane.showConfirmDialog(frame, "File changed. Save before progress?", "", JOptionPane.YES_NO_CANCEL_OPTION);
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
			final int i = JOptionPane.showConfirmDialog(frame, "File changed. Save before progress?", "", JOptionPane.YES_NO_CANCEL_OPTION);
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
		app.getAssetManager().registerLocator(".", FileLocator.class);
		try(final Reader in = new FileReader(file)) {
			final Path openPath = openedFolder.source().toPath();
			final File relativePath = new File(openPath.relativize(file.toPath()).toString());
			
			app.getAssetManager().clearCache();
			final Spatial spatial = app.getAssetManager().loadModel(relativePath.getPath());
			
			final SetableMutableSingleValueModel<Editable> editable = new SetableMutableSingleValueModelImpl<>();
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
			JOptionPane.showMessageDialog(frame, "Could not load file: " + e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void saveFile() {
		final DefaultTreeModel model = (DefaultTreeModel)componentTree.getModel();
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
			final int i = JOptionPane.showConfirmDialog(frame, "File changed. Save before progress?", "", JOptionPane.YES_NO_CANCEL_OPTION);
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
		final DefaultTreeModel model = (DefaultTreeModel) componentTree.getModel();
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
		
		valuesTable.revalidate();
		valuesTable.repaint();
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
		
		valuesTable.revalidate();
		valuesTable.repaint();
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
		
		valuesTable.revalidate();
		valuesTable.repaint();
	}
	
	public JFrame getFrame() {
		return (frame);
	}
	
	private void setObject(final Editable obj) {
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(obj);
		
		if(obj instanceof NodeEditable) {
			final core.editables.NodeEditable asNode = (core.editables.NodeEditable) obj;
			for(int i = 0, size = asNode.getChildCount(); i < size; i++) {
				final Editable child = asNode.getChild(i);
				setObject0(child, rootNode);
			}
		} else if(obj instanceof GeometryEditable) {
			rootNode.add(new DefaultMutableTreeNode(((GeometryEditable)obj).getMesh()));
		}
		
		componentTree.setModel(new DefaultTreeModel(rootNode) {
			@Override
			public void valueForPathChanged(final TreePath path, final Object newValue) {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
				final Editable obj = (Editable) node.getUserObject();
				//obj.setName(newValue != null ? newValue.toString() : "null");
				nodeChanged(node);
			}
		});
		
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				tdview.setObject(obj);
			}
		});
	}
	private void setObject0(final Editable obj, final DefaultMutableTreeNode parent) {
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj);
		parent.add(node);
		
		if(obj instanceof core.editables.NodeEditable) {
			final core.editables.NodeEditable asNode = (core.editables.NodeEditable) obj;
			for(int i = 0, size = asNode.getChildCount(); i < size; i++) {
				final Editable child = asNode.getChild(i);
				setObject0(child, node);
			}
		} else if(obj instanceof GeometryEditable) {
			node.add(new DefaultMutableTreeNode(((GeometryEditable)obj).getMesh()));
		}
	}
	
	/*private void includeObject(final Editable obj, final DefaultMutableTreeNode parent) {
		final Editable parentObject = (Editable) parent.getUserObject();
		
		final DefaultMutableTreeNode curNode = new DefaultMutableTreeNode(obj);
		parent.add(curNode);
		
		if(obj instanceof core.editables.NodeEditable) {
			final core.editables.NodeEditable asNode = (core.editables.NodeEditable) obj;
			for(int i = 0, size = asNode.getChildCount(); i < size; i++) {
				final Editable child = asNode.getChild(i);
				setObject0(child, curNode);
			}
		}
		
		((DefaultTreeModel)componentTree.getModel()).nodeStructureChanged(parent);
		tdview.addObject(obj, parentObject);
	}*/
	
	private void addObject(final SpatialEditable obj, final DefaultMutableTreeNode parent) {
		((NodeEditable) parent.getUserObject()).addChild(obj);
		parent.add(addObject0(obj));
		((DefaultTreeModel)componentTree.getModel()).nodeStructureChanged(parent);
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
				
				((DefaultTreeModel)componentTree.getModel()).nodeStructureChanged(parent);
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
	
	private File getFreeFileName(final File source, final String s, final String extension) {
		final File newFile = new File(source, s + extension);
		if(!newFile.exists()) {
			return(newFile);
		}
		
		final StringBuilder b = new StringBuilder(s);
		b.append(".");
		final int size = s.length() + 1;
		for(int i = 0, size2 = Integer.MAX_VALUE; i < size2; i++) {
			b.setLength(size);
			b.append(i);
			b.append(extension);
			
			final File testFile = new File(source, b.toString());
			if(!testFile.exists()) {
				return(testFile);
			}
		}
		throw new IllegalArgumentException("Found not a single free file name.");
	}
	
	public void show() {
		rearrangeWindows();
		show(ui.getFrame());
		show(ui.getSceneGraphDialog());
		show(ui.getPropertiesDialog());
		show(ui.getFilesDialog());
		show(ui.getCameraWindow());
		show(ui.getThreeDTransformWindow());
	}
	public void show(final Window window) {
		window.pack();
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
		
		final JFrame toolbarWindow = ui.getFrame();
		toolbarWindow.pack();
		setWindowLocation(toolbarWindow,
				new Point(
						scaledscreenSize.width / 2 - toolbarWindow.getWidth() / 2,
						scaledscreenSize.height / 2 - toolbarWindow.getHeight() / 2 - threeDViewHeight[0] / 2)
		);
		
		final JDialog filesWindow = ui.getFilesDialog();
		filesWindow.pack();
		setWindowLocation(filesWindow,
				new Point(
						scaledscreenSize.width / 2 - filesWindow.getWidth() / 2 - threeDViewWidth[0] / 2,
						scaledscreenSize.height / 2 - filesWindow.getHeight())
				);
		
		final JDialog sceneGraphWindow = ui.getSceneGraphDialog();
		sceneGraphWindow.pack();
		setWindowLocation(sceneGraphWindow,
				new Point(
						scaledscreenSize.width / 2 - sceneGraphWindow.getWidth() / 2 - threeDViewWidth[0] / 2,
						scaledscreenSize.height / 2
		));
		
		final JDialog propertyWindow = ui.getPropertiesDialog();
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
}
