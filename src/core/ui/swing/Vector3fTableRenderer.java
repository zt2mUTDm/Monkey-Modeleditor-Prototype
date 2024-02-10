package core.ui.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.jme3.math.Vector3f;

public final class Vector3fTableRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		final Vector3f cast = (Vector3f) value;
		
		final JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText(cast.getX() + ", " + cast.getY() + ", " + cast.getZ());
		return(label);
	}
}
