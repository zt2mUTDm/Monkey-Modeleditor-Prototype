package core.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class DirectoryTree {
	private final DefaultTreeModel model;
	private final JTree tree;
	
	private File currentDirectory;
	
	private final Collection<DataSink<? super File>> fileOpenedListeners;
	private final Collection<DataSink<? super DirectoryTree>> folderChangedListeners;
	
	public DirectoryTree() {
		this(null);
	}
	public DirectoryTree(final File dir) {
		this.fileOpenedListeners = new LinkedList<>();
		this.folderChangedListeners = new LinkedList<>();
		
		model = new DefaultTreeModel(null);
		tree = new JTree(model);
		tree.setRootVisible(false);
		tree.setCellRenderer(new FileTreeCellRenderer());
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if((e.getClickCount() % 2) != 0) {
					return;
				}
				
				final int x = e.getX();
				final int y = e.getY();
				
				final TreePath path = tree.getPathForLocation(x, y);
				if(path == null) {
					return;
				}
				
				tree.setSelectionPath(path);
				final DefaultMutableTreeNode last = (DefaultMutableTreeNode) path.getLastPathComponent();
				final FileTreeWrappler wrap = (FileTreeWrappler) last.getUserObject();
				final File f = wrap.getFile();
				
				if(f == null || f.isDirectory()) {
					currentDirectory = f;
					refresh();
					fireFolderChangedListeners(DirectoryTree.this);
				} else if(!f.exists()) {
					return;
				} else {
					fireFileOpenedListeners(f);
				}
			}
		});
		
		open(dir);
	}
	
	public void open(final File dir) {
		if(dir != null) {
			final File dirAbs = dir.getAbsoluteFile();
			this.currentDirectory = dirAbs;
		} else {
			this.currentDirectory = null;
		}
		refresh();
	}
	public void refresh() {
		if(currentDirectory != null) {
			if(!currentDirectory.exists() || !currentDirectory.isDirectory()) {
				return;
			}
		}
		
		final DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode("root");
		
		final File[] content;
		if(currentDirectory != null) {
			rootTreeNode.add(new DefaultMutableTreeNode(new FileTreeWrappler(currentDirectory.getParentFile(), "..")));
			content = currentDirectory.listFiles();
		} else {
			content = File.listRoots();
		}
		
		Arrays.sort(content, new FileSorter());
		
		for(final File file:content) {
			rootTreeNode.add(new DefaultMutableTreeNode(new FileTreeWrappler(file)));
		}
		model.setRoot(rootTreeNode);
	}
	
	public File getCurrentDirectory() {
		return (currentDirectory);
	}
	
	public void addFileOpenedListener(final DataSink<? super File> l) {
		fileOpenedListeners.add(Requires.notNull(l, "l == null"));
	}
	public void removeFileOpenedListener(final DataSink<? super File> l) {
		fileOpenedListeners.remove(Requires.notNull(l, "l == null"));
	}
	private void fireFileOpenedListeners(final File file) {
		for(final DataSink<? super File> l:fileOpenedListeners) {
			l.sink(file);
		}
	}
	
	public void addFolderChangedListener(final DataSink<? super DirectoryTree> l) {
		folderChangedListeners.add(Requires.notNull(l, "l == null"));
	}
	public void removeFolderChangedListener(final DataSink<? super DirectoryTree> l) {
		folderChangedListeners.remove(Requires.notNull(l, "l == null"));
	}
	private void fireFolderChangedListeners(final DirectoryTree source) {
		for(final DataSink<? super DirectoryTree> l:folderChangedListeners) {
			l.sink(source);
		}
	}
	
	public JTree getTree() {
		return (tree);
	}
}
