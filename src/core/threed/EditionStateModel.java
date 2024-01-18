package core.threed;

import core.GridMode;
import online.money_daisuki.api.misc.mapping.Mapping;

public interface EditionStateModel {
	
	float getGridSize();
	
	GridMode getGridMode();
	
	void execute(Mapping<Runnable, Runnable> command);
	
	void executeTemporary(Mapping<Runnable, Runnable> command);
	
}
