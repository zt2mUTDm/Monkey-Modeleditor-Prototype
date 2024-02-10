package core.ui;

import java.io.File;

import online.money_daisuki.api.base.Requires;

public final class FileTreeWrappler implements CharSequence {
	private final File f;
	private final String text;
	
	public FileTreeWrappler(final File f) {
		this.f = f;
		this.text = f != null ? f.getName().isEmpty() ? f.getAbsolutePath() : f.getName() : "null";
	}
	public FileTreeWrappler(final File f, final String text) {
		this.f = f;
		this.text = Requires.notNull(text, "text == null");
	}
	public File getFile() {
		return (f);
	}
	
	@Override
	public String toString() {
		return(text);
	}
	@Override
	public int length() {
		return(text.length());
	}
	@Override
	public char charAt(final int index) {
		return(text.charAt(index));
	}
	@Override
	public CharSequence subSequence(final int start, final int end) {
		return(text.subSequence(start, end));
	}
}
