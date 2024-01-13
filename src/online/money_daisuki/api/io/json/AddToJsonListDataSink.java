package online.money_daisuki.api.io.json;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

/**
 * {@link DataSink} that has a {@link MutableJsonList} and adds received
 * {@link JsonElement}s to.
 * 
 * @author (c) Money Daisuki Online
 */
public final class AddToJsonListDataSink implements DataSink<JsonElement> {
	private final MutableJsonList parent;
	
	public AddToJsonListDataSink(final MutableJsonList parent) {
		this.parent = Requires.notNull(parent, "parent == null");
	}
	@Override
	public void sink(final JsonElement value) {
		parent.add(Requires.notNull(value, "value == null"));
	}
}
