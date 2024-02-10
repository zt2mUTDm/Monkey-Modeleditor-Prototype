package core.editables;

import java.util.Collection;

public interface Editable {
	
	void setSelected(boolean b);
	
	Collection<EditionMode> getEditionModes();
	
}
