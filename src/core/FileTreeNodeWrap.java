package core;

import java.io.File;

import online.money_daisuki.api.base.Requires;

public final class FileTreeNodeWrap implements CharSequence {
	private final File file;
	private final String name;
	
	public FileTreeNodeWrap(final File file) {
		this.file = Requires.notNull(file, "file != null");
		this.name = file.getName();
	}
	public File getFile() {
		return (file);
	}
	@Override
	public String toString() {
		return(name);
	}
	@Override
	public int length() {
		return(name.length());
	}
	@Override
	public char charAt(final int index) {
		return(name.charAt(index));
	}
	@Override
	public CharSequence subSequence(final int start, final int end) {
		return(name.subSequence(start, end));
	}
}
