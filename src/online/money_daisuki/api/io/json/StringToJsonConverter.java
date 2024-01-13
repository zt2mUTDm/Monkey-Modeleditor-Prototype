package online.money_daisuki.api.io.json;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;

public final class StringToJsonConverter<R> implements Converter<String, R> {
	private final Converter<? super JsonElement, ? extends R> parent;
	
	public StringToJsonConverter(final Converter<? super JsonElement, ? extends R> parent) {
		this.parent = Requires.notNull(parent, "parent == null");		
	}	
	@Override
	public R convert(final String value) {
		return(parent.convert(new JsonStringDataElement(Requires.notNull(value, "value == null"))));
	}
}
