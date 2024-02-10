package core.ui.swing;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jme3.math.Vector2f;

public final class Vector2fTableEditor extends DefaultCellEditor {
	private Vector2f outValue;
	
	public Vector2fTableEditor() {
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
				
				outValue = new Vector2f(x, y);
			} else {
				final float f = Float.parseFloat(text);
				outValue = new Vector2f(f, f);
			}
			super.fireEditingStopped();
		} catch(final Throwable e) {
			JOptionPane.showMessageDialog(null, "No vector: " + e.getMessage());
			super.fireEditingCanceled();
		}
	}
	@Override
	public Vector2f getCellEditorValue() {
		return(outValue);
	}
	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
			final int row, final int column) {
		final Vector2f vec = (Vector2f) value;
		final String text = vec.getX() + ", " + vec.getY();
		
		final JTextField field = (JTextField) super.getTableCellEditorComponent(table, text, isSelected, row, column);
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				field.selectAll();
			}
		});
		return(field);
	}
}
