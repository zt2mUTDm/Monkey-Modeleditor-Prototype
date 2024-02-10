package core.ui.swing;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jme3.math.FastMath;
import com.simsilica.mathd.Quatd;

public final class QuatdTableEditor extends DefaultCellEditor {
	private Quatd outValue;
	
	public QuatdTableEditor() {
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
				
				outValue = new Quatd().fromAngles(new double[] {
					x * FastMath.DEG_TO_RAD,
					y * FastMath.DEG_TO_RAD,
					z * FastMath.DEG_TO_RAD
				});
			} else {
				final double d = Double.parseDouble(text);
				outValue = new Quatd().fromAngles(new double[] {
						d * FastMath.DEG_TO_RAD,
						d * FastMath.DEG_TO_RAD,
						d * FastMath.DEG_TO_RAD
				});
			}
			super.fireEditingStopped();
		} catch(final Throwable e) {
			JOptionPane.showMessageDialog(null, "No vector: " + e.getMessage());
			super.fireEditingCanceled();
		}
	}
	@Override
	public Quatd getCellEditorValue() {
		return(outValue);
	}
	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
			final int row, final int column) {
		final Quatd cast = (Quatd) value;
		final double[] angels = cast.toAngles(new double[3]);
		
		angels[0]*= FastMath.RAD_TO_DEG;
		angels[1]*= FastMath.RAD_TO_DEG;
		angels[2]*= FastMath.RAD_TO_DEG;
		
		final String text = angels[0] + ", " + angels[1] + ", "+ angels[2];
		return(super.getTableCellEditorComponent(table, text, isSelected, row, column));
	}
}
