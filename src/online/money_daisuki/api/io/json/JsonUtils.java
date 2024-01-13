package online.money_daisuki.api.io.json;

import online.money_daisuki.api.base.Requires;

public final class JsonUtils {
	private JsonUtils() {
		throw new UnsupportedOperationException("should never reaches");
	}
	public static String[] jsonStringListToArray(final JsonList list) {
		final int size = Requires.notNull(list, "list == null").size();
		final String[] s = new String[size];
		for(int i = 0; i < size; i++) {
			s[i] = list.asData().asString();
		}
		return(s);
	}
}
