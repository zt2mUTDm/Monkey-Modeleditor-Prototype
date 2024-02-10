package core.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public final class FileTreeCellRenderer extends DefaultTreeCellRenderer {
	@Override
	public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded,
			final boolean leaf, final int row, final boolean hasFocus) {
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		final Object userObject = node.getUserObject();
		
		if(userObject instanceof FileTreeWrappler) {
			final File file = ((FileTreeWrappler) node.getUserObject()).getFile();
			return(super.getTreeCellRendererComponent(tree, value, selected, expanded, file != null ? !file.isDirectory() : false, row, hasFocus));
		} else {
			return(super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus));
		}
	}
}
