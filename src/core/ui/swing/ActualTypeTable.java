package core.ui.swing;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import online.money_daisuki.api.base.Requires;

public final class ActualTypeTable extends JTable {
	@Override
	public final TableCellEditor getCellEditor(final int row, final int column) {
		final Object obj = getModel().getValueAt(row, column);
		assertNotNull(obj);
		
		final TableCellEditor editor = getDefaultEditor(obj.getClass());
		if(editor != null) {
			return(editor);
		} else {
			return(getDefaultEditor(Object.class));
		}
	}
	@Override
	public final TableCellRenderer getCellRenderer(final int row, final int column) {
		final Object obj = getModel().getValueAt(row, column);
		assertNotNull(obj);
		
		final TableCellRenderer renderer = getDefaultRenderer(obj.getClass());
		if(renderer != null) {
			return(renderer);
		} else {
			return(getDefaultRenderer(Object.class));
		}
	}
	private void assertNotNull(final Object obj) {
		Requires.notNull(obj, "This table doesn't support null-values");
	} 
}
