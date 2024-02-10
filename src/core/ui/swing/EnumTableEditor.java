package core.ui.swing;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import com.simsilica.lemur.Axis;

public final class EnumTableEditor extends DefaultCellEditor {
	public EnumTableEditor() {
		super(new JComboBox<>(new DefaultComboBoxModel<>()));
	}
	
	@SuppressWarnings("unchecked")
	private DefaultComboBoxModel<Enum<?>> getModel() {
		return (DefaultComboBoxModel<Enum<?>>) (((JComboBox<Enum<?>>) getComponent()).getModel());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
		final JComboBox<Enum<?>> box = (JComboBox<Enum<?>>) super.getTableCellEditorComponent(table, value, isSelected, row, column);
		
		final Enum<?> val = (Enum<Axis>) value;
		try {
			final Enum<?>[] e = (Enum<?>[]) val.getClass().getMethod("values").invoke(null);
			final DefaultComboBoxModel<Enum<?>> model = getModel();
			model.removeAllElements();
			model.addAll(Arrays.asList(e));
			model.setSelectedItem(val);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException(e);
		}
		
		
		return(box);
	}
}
