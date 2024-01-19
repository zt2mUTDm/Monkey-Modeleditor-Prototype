package core.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import online.money_daisuki.api.base.Requires;

public final class DirectoryFileFilter extends FileFilter {
	private final String description;
	
	public DirectoryFileFilter() {
		this("Directory");
	}
	public DirectoryFileFilter(final String description) {
		this.description = Requires.notNull(description, "description == null");
	}
	@Override
	public boolean accept(final File f) {
		return(f.isDirectory());
	}
	@Override
	public String getDescription() {
		return(description);
	}
}
