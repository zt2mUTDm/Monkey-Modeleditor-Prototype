package core.ui;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public final class ActualTypeTable extends JTable {
	@Override
	public final TableCellEditor getCellEditor(final int row, final int column) {
		final Object obj = getModel().getValueAt(row, column);
		if(obj == null) {
			throw new IllegalArgumentException("This table doesn't support null-values");
		}
		
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
		if(obj == null) {
			throw new IllegalArgumentException("This table doesn't support null-values");
		}
		
		final TableCellRenderer renderer = getDefaultRenderer(obj.getClass());
		if(renderer != null) {
			return(renderer);
		} else {
			return(getDefaultRenderer(Object.class));
		}
	} 
}
