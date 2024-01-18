package core.ui;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public final class IntegerTableEditor extends DefaultCellEditor {
	private Integer outValue;
	
	public IntegerTableEditor() {
		super(new JTextField());
		((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
	}
	@Override
	protected void fireEditingStopped() {
		try {
			outValue = Integer.parseInt(((JTextField) getComponent()).getText());
			super.fireEditingStopped();
		} catch(final NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Could not parse integer value");
			super.fireEditingCanceled();
		}
	}
	@Override
	public Integer getCellEditorValue() {
		return(outValue);
	}
}
