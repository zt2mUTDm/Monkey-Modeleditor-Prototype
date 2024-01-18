package core.editables;

import core.threed.EditionState;
import core.threed.EditionStateModel;
import online.money_daisuki.api.misc.mapping.Mapping;

public interface EditionMode {
	
	String getName();
	
	Object get();
	
	boolean isEditableByTable();
	
	boolean isEditableByThreeDView();
	
	Mapping<Runnable, Runnable> createChangeCommand(Editable source, Object aValue);
	
	EditionState createEditionState(EditionStateModel model);
	
}
