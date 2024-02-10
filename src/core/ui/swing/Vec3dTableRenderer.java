package core.ui.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.simsilica.mathd.Vec3d;

public final class Vec3dTableRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		final Vec3d cast = (Vec3d) value;
		
		final JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText(cast.x + ", " + cast.y + ", " + cast.z);
		return(label);
	}
}
