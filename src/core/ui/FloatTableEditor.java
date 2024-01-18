package core.ui;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public final class FloatTableEditor extends DefaultCellEditor {
	private Float outValue;
	
	public FloatTableEditor() {
		super(new JTextField());
		((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
	}
	@Override
	protected void fireEditingStopped() {
		try {
			outValue = Float.parseFloat(((JTextField) getComponent()).getText());
			super.fireEditingStopped();
		} catch(final NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Could not parse float value");
			super.fireEditingCanceled();
		}
	}
	@Override
	public Float getCellEditorValue() {
		return(outValue);
	}
}
