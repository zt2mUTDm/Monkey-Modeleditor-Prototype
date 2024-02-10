package core.ui.swing;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.simsilica.mathd.Vec3d;

public final class Vec3dTableEditor extends DefaultCellEditor {
	private Vec3d outValue;
	
	public Vec3dTableEditor() {
		super(new JTextField());
	}
	@Override
	protected void fireEditingStopped() {
		try {
			final String text = ((JTextField) getComponent()).getText();
			if(text.contains(",")) {
				final String[] split = text.split(",");
				final double x = Double.parseDouble(split[0]);
				final double y = Double.parseDouble(split[1]);
				final double z = Double.parseDouble(split[2]);
				
				outValue = new Vec3d(x, y, z);
			} else {
				final double f = Double.parseDouble(text);
				outValue = new Vec3d(f, f, f);
			}
			super.fireEditingStopped();
		} catch(final Throwable e) {
			JOptionPane.showMessageDialog(null, "No vector: " + e.getMessage());
			super.fireEditingCanceled();
		}
	}
	@Override
	public Object getCellEditorValue() {
		return(outValue);
	}
	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
			final int row, final int column) {
		final Vec3d vec = (Vec3d) value;
		final String text = vec.x + ", " + vec.y + ", "+ vec.z;
		
		final JTextField field = (JTextField) super.getTableCellEditorComponent(table, text, isSelected, row, column);
		field.selectAll();
		return(field);
	}
}
