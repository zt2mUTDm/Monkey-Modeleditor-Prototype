package core.setting;

import online.money_daisuki.api.base.ValueChangedHandler;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.io.json.MutableJsonMap;

public interface SpecificSetting {
	
	void load(JsonMap map);
	
	void save(MutableJsonMap map);
	
	void setDefaults();
	
	
	void addValueChangeListener(ValueChangedHandler<Object> l);
	
	void removeValueChangeListener(ValueChangedHandler<Object> l);
	
}
