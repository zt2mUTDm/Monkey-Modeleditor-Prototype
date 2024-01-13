package core.editables;

import java.io.IOException;
import java.io.Reader;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;

public final class EditableLoader implements DataSource<Editable> {
	private final Reader in;
	private final SimpleApplication app;
	
	public EditableLoader(final Reader in, final SimpleApplication app) {
		this.in = Requires.notNull(in, "in == null");
		this.app = Requires.notNull(app, "app == null");
	}
	
	@Override
	public Editable source() {
		try {
			final JsonDecoder dec = new JsonDecoder(in);
			final JsonElement e = dec.decode();
			if(e == null) {
				return(null);
			}
			return(load(e.asMap()));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	public Editable load(final JsonMap map) throws IOException {
		final Editable edit = loadEditable(map);
		
		if(map.containsKey("name")) {
			final String name = map.get("name").asData().asString();
			edit.setName(name);
		} else {
			edit.setName("");
		}
		
		if(map.containsKey("translation")) {
			final JsonList location = map.get("translation").asList();
			final Vector3f locVec = new Vector3f(location.get(0).asData().asNumber().asBigDecimal().floatValue(),
					location.get(1).asData().asNumber().asBigDecimal().floatValue(),
					location.get(2).asData().asNumber().asBigDecimal().floatValue());
			edit.setLocalTranslation(locVec);
		}
		
		if(map.containsKey("rotation")) {
			final JsonList rotation = map.get("rotation").asList();
			final Quaternion rotationQuat = new Quaternion().fromAngles(new float[] {
					rotation.get(0).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD,
					rotation.get(1).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD,
					rotation.get(2).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD
			});
			edit.setLocalRotation(rotationQuat);
		}
		
		if(map.containsKey("scale")) {
			final JsonList scale = map.get("scale").asList();
			final Vector3f scaleVec = new Vector3f(scale.get(0).asData().asNumber().asBigDecimal().floatValue(),
					scale.get(1).asData().asNumber().asBigDecimal().floatValue(),
					scale.get(2).asData().asNumber().asBigDecimal().floatValue());
			edit.setLocalScale(scaleVec);
		}
		
		if(map.containsKey("childs")) {
			final JsonList childs = map.get("childs").asList();
			for(final JsonElement e:childs) {
				edit.addChild(load(e.asMap()));
			}
		}
		return(edit);
	}
	private Editable loadEditable(final JsonMap map) {
		final String type = map.get("type").asData().asString();
		switch(type) {
			case("node"):
				return(new NodeEditable(app.getAssetManager()));
			case("model"):
				return(createModelEditable(map));
			default:
				throw new IllegalArgumentException("Unknown type: " + type);
		}
	}
	private Editable createModelEditable(final JsonMap map) {
		final String url = map.get("url").asData().asString();
		return(new ModelEditable(app, url));
	}
}
