package core.ui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;

public final class QuaternionTableEditor extends DefaultCellEditor {
	private Quaternion outValue;
	
	public QuaternionTableEditor() {
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
				
				outValue = new Quaternion().fromAngles(new float[] {
					x * FastMath.DEG_TO_RAD,
					y * FastMath.DEG_TO_RAD,
					z * FastMath.DEG_TO_RAD
				});
			} else {
				final float f = Float.parseFloat(text);
				outValue = new Quaternion().fromAngles(new float[] {
						f * FastMath.DEG_TO_RAD,
						f * FastMath.DEG_TO_RAD,
						f * FastMath.DEG_TO_RAD
				});
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
		final Quaternion cast = (Quaternion) value;
		final float[] angels = cast.toAngles(new float[3]);
		
		angels[0]*= FastMath.RAD_TO_DEG;
		angels[1]*= FastMath.RAD_TO_DEG;
		angels[2]*= FastMath.RAD_TO_DEG;
		
		final String text = angels[0] + ", " + angels[1] + ", "+ angels[2];
		return(super.getTableCellEditorComponent(table, text, isSelected, row, column));
	}
}
