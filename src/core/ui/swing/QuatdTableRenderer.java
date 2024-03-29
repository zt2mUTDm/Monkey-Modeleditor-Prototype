package core.ui.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.jme3.math.FastMath;
import com.simsilica.mathd.Quatd;

public final class QuatdTableRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		final Quatd cast = (Quatd) value;
		final double[] angels = cast.toAngles(new double[3]);
		
		final JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText(angels[0] * FastMath.RAD_TO_DEG + ", " + angels[1] * FastMath.RAD_TO_DEG + ", " +
				angels[2] * FastMath.RAD_TO_DEG);
		return(label);
	}
}
