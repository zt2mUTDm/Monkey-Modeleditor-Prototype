package core.editables;

import java.util.Collection;
import java.util.Collections;

import online.money_daisuki.api.base.Requires;

public final class StringEditable implements Editable, CharSequence {
	private final String s;
	
	public StringEditable(final String s) {
		this.s = Requires.notNull(s, "s == null");
	}
	
	
	@Override
	public void setSelected(final boolean b) {
		
	}
	
	@Override
	public Collection<EditionMode> getEditionModes() {
		return(Collections.emptySet());
	}
	
	@Override
	public char charAt(final int index) {
		return(s.charAt(index));
	}
	
	@Override
	public int length() {
		return(s.length());
	}
	
	@Override
	public CharSequence subSequence(final int start, final int end) {
		return(s.subSequence(start, end));
	}
	
	@Override
	public String toString() {
		return (s);
	}
}
