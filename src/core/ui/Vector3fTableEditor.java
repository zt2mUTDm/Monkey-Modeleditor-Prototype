package core.ui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jme3.math.Vector3f;

public final class Vector3fTableEditor extends DefaultCellEditor {
	private Vector3f outValue;
	
	public Vector3fTableEditor() {
		super(new JTextField());
	}
	@Override
	protected void fireEditingStopped() {
		try {
			final String text = ((JTextField) getComponent()).getText();
			if(text.contains(",")) {
				final String[] split = text.split(",");
				final float x = Float.parseFloat(split[0]);
				final float y = Float.parseFloat(split[1]);
				final float z = Float.parseFloat(split[2]);
				
				outValue = new Vector3f(x, y, z);
			} else {
				final float f = Float.parseFloat(text);
				outValue = new Vector3f(f, f, f);
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
		final Vector3f vec = (Vector3f) value;
		final String text = vec.getX() + ", " + vec.getY() + ", "+ vec.getZ();
		return(super.getTableCellEditorComponent(table, text, isSelected, row, column));
	}
}
