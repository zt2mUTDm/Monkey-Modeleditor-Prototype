package core.ui.swing;

import java.util.Comparator;
import java.util.Map.Entry;

public final class StringEntryKeySorter implements Comparator<Entry<String, ?>> {
	@Override
	public int compare(final Entry<String, ?> o1, final Entry<String, ?> o2) {
		final String s1 = o1.getKey();
		final String s2 = o2.getKey();
		
		return(s1.compareTo(s2));
	}
}
