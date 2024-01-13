package online.money_daisuki.api.io.json;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;

public final class JsonToStringConverter<R> implements Converter<JsonElement, R> {
	private final Converter<? super String, ? extends R> parent;
	
	public JsonToStringConverter(final Converter<? super String, ? extends R> parent) {
		this.parent = Requires.notNull(parent, "parent == null");		
	}
	@Override
	public R convert(final JsonElement value) {
		return(parent.convert(value.asData().asString()));
	}
}
