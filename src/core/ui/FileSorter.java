package core.ui;

import java.io.File;
import java.util.Comparator;

public final class FileSorter implements Comparator<File> {
	@Override
	public int compare(final File o1, final File o2) {
		final int type1 = getType(o1);
		final int type2 = getType(o2);
		
		if(type1 == type2) {
			return(o1.getName().compareTo(o2.getName()));
		} else {
			return(type1 - type2);
		}
	}
	private int getType(final File o) {
		if(o.isDirectory()) {
			return(0);
		} else if(o.isFile()) {
			return(1);
		} else {
			return(2);
		}
	}
}
