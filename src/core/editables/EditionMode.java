package core.editables;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public enum EditionMode {
	NAME {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final String curName = obj.getName();
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					obj.setName(data);
				}
			}, new Runnable() {
				@Override
				public void run() {
					obj.setName(curName);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			return(obj.getName());
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(String.class);
		}
		
		@Override
		public String getName() {
			return("Name");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	LOCAL_TRANSLATION {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final float[] arr = parse3Numbers(data);
			if(arr == null) {
				return(null);
			}
			
			final Vector3f curVec = new Vector3f(obj.getLocalTranslation());
			final Vector3f newVec = new Vector3f(arr[0], arr[1], arr[2]);
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					obj.setLocalTranslation(newVec);
				}
			}, new Runnable() {
				@Override
				public void run() {
					obj.setLocalTranslation(curVec);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final Vector3f vec = obj.getLocalTranslation();
			return(vec.x + ", " + vec.y + ", " + vec.z);
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Vector3f.class);
		}
		
		@Override
		public String getName() {
			return("Local translation");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(true);
		}
	},
	LOCAL_ROTATION {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final float[] arr = parse3Numbers(data);
			if(arr == null) {
				return(null);
			}
			
			for(int i = 0, size = arr.length; i < size; i++) {
				arr[i]*= FastMath.DEG_TO_RAD;
			}
			
			final Quaternion curVec = new Quaternion(obj.getLocalRotation());
			final Quaternion newVec = new Quaternion().fromAngles(arr);
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					obj.setLocalRotation(newVec);
				}
			}, new Runnable() {
				@Override
				public void run() {
					obj.setLocalRotation(curVec);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final Quaternion quat = obj.getLocalRotation();
			final float[] angles = quat.toAngles(null);
			return(angles[0] * FastMath.RAD_TO_DEG + ", " + angles[1] * FastMath.RAD_TO_DEG + ", " +
					angles[2] * FastMath.RAD_TO_DEG);
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Quaternion.class);
		}
		
		@Override
		public String getName() {
			return("Local rotation");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(true);
		}
	},
	LOCAL_SCALE {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final float[] arr = parse3Numbers(data);
			if(arr == null) {
				return(null);
			}
			
			final Vector3f curVec = new Vector3f(obj.getLocalScale());
			final Vector3f newVec = new Vector3f(arr[0], arr[1], arr[2]);
			
			newVec.maxLocal(new Vector3f(0, 0, 0));
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					obj.setLocalScale(newVec);
				}
			}, new Runnable() {
				@Override
				public void run() {
					obj.setLocalScale(curVec);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final Vector3f vec = obj.getLocalScale();
			return(vec.x + ", " + vec.y + ", " + vec.z);
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Vector3f.class);
		}
		
		@Override
		public String getName() {
			return("Local scale");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(true);
		}
	},
	WORLD_TRANSLATION {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public String get(final Editable obj) {
			final Vector3f vec = obj.getWorldTranslation();
			return(vecToStr(vec));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(false);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Quaternion.class);
		}
		
		@Override
		public String getName() {
			return("World translation");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	LOOPING {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final AudioNodeEditable edit = (AudioNodeEditable) obj;
			final boolean old = edit.isLooping();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					edit.setLooping(Boolean.parseBoolean(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					edit.setLooping(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final AudioNodeEditable edit = (AudioNodeEditable) obj;
			return(String.valueOf(edit.isLooping()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Boolean.class);
		}
		
		@Override
		public String getName() {
			return("Looping");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	EXTENDS {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final BoxEditable edit = (BoxEditable) obj;
			final Vector3f old = edit.getExtends();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					edit.setExtends(strToVec(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					edit.setExtends(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final BoxEditable edit = (BoxEditable) obj;
			return(vecToStr(edit.getExtends()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Vector3f.class);
		}
		
		@Override
		public String getName() {
			return("Extends");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	COLOR {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasColor cast = (HasColor) obj;
			final ColorRGBA old = cast.getColor();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setColor(strToColor(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setColor(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasColor cast = (HasColor) obj;
			return(colorToStr(cast.getColor()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(ColorRGBA.class);
		}
		
		@Override
		public String getName() {
			return("Color");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	STRIP {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final BoxEditable edit = (BoxEditable) obj;
			final boolean old = edit.isStrip();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					edit.setStrip(Boolean.parseBoolean(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					edit.setStrip(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final BoxEditable edit = (BoxEditable) obj;
			return(String.valueOf(edit.isStrip()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Boolean.class);
		}
		
		@Override
		public String getName() {
			return("Strip");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	AXIS_SAMPLES {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasAxisSamples cast = (HasAxisSamples) obj;
			final int old = cast.getAxisSamples();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setAxisSamples(Integer.parseInt(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setAxisSamples(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasAxisSamples cast = (HasAxisSamples) obj;
			return(String.valueOf(cast.getAxisSamples()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Integer.class);
		}
		
		@Override
		public String getName() {
			return("Axis samples");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	PLANES {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasPlanes cast = (HasPlanes) obj;
			final int old = cast.getPlanes();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setPlanes(Integer.parseInt(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setPlanes(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasPlanes cast = (HasPlanes) obj;
			return(String.valueOf(cast.getPlanes()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Integer.class);
		}
		
		@Override
		public String getName() {
			return("Planes");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	RADIAL_SAMPLES {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasRadialSamples cast = (HasRadialSamples) obj;
			final int old = cast.getRadialSamples();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setRadialSamples(Integer.parseInt(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setRadialSamples(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasRadialSamples cast = (HasRadialSamples) obj;
			return(String.valueOf(cast.getRadialSamples()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Integer.class);
		}
		
		@Override
		public String getName() {
			return("Radial samples");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	CIRCLE_SAMPLES {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasCircleSamples cast = (HasCircleSamples) obj;
			final int old = cast.getCircleSamples();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setCircleSamples(Integer.parseInt(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setCircleSamples(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasCircleSamples cast = (HasCircleSamples) obj;
			return(String.valueOf(cast.getCircleSamples()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Integer.class);
		}
		
		@Override
		public String getName() {
			return("Circle samples");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	Z_SAMPLES {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasZSamples cast = (HasZSamples) obj;
			final int old = cast.getZSamples();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setZSamples(Integer.parseInt(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setZSamples(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasZSamples cast = (HasZSamples) obj;
			return(String.valueOf(cast.getZSamples()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Integer.class);
		}
		
		@Override
		public String getName() {
			return("Z samples");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	RADIUS {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasRadius cast = (HasRadius) obj;
			final float old = cast.getRadius();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setRadius(Float.parseFloat(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setRadius(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasRadius cast = (HasRadius) obj;
			return(String.valueOf(cast.getRadius()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Float.class);
		}
		
		@Override
		public String getName() {
			return("Radius");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	INNER_RADIUS {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasInnerRadius cast = (HasInnerRadius) obj;
			final float old = cast.getInnerRadius();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setInnerRadius(Float.parseFloat(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setInnerRadius(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasInnerRadius cast = (HasInnerRadius) obj;
			return(String.valueOf(cast.getInnerRadius()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Float.class);
		}
		
		@Override
		public String getName() {
			return("Inner radius");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	OUTER_RADIUS {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasOuterRadius cast = (HasOuterRadius) obj;
			final float old = cast.getOuterRadius();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setOuterRadius(Float.parseFloat(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setOuterRadius(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasOuterRadius cast = (HasOuterRadius) obj;
			return(String.valueOf(cast.getOuterRadius()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Float.class);
		}
		
		@Override
		public String getName() {
			return("Outer radius");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	HEIGHT {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasHeight cast = (HasHeight) obj;
			final float old = cast.getHeight();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setHeight(Float.parseFloat(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setHeight(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasHeight cast = (HasHeight) obj;
			return(String.valueOf(cast.getHeight()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Float.class);
		}
		
		@Override
		public String getName() {
			return("Height");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	CLOSED {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasClosed cast = (HasClosed) obj;
			final boolean old = cast.isClosed();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setClosed(Boolean.parseBoolean(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setClosed(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasClosed cast = (HasClosed) obj;
			return(String.valueOf(cast.isClosed()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Boolean.class);
		}
		
		@Override
		public String getName() {
			return("Closed");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	INVERTED {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasInverted cast = (HasInverted) obj;
			final boolean old = cast.isInverted();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setInverted(Boolean.parseBoolean(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setInverted(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasInverted cast = (HasInverted) obj;
			return(String.valueOf(cast.isInverted()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Boolean.class);
		}
		
		@Override
		public String getName() {
			return("Inverted");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	USE_EVEN_SLICES {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasUseEvenSlices cast = (HasUseEvenSlices) obj;
			final boolean old = cast.isUseEvenSlices();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setUseEvenSlices(Boolean.parseBoolean(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setUseEvenSlices(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasUseEvenSlices cast = (HasUseEvenSlices) obj;
			return(String.valueOf(cast.isUseEvenSlices()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Boolean.class);
		}
		
		@Override
		public String getName() {
			return("Use even slices");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	},
	INTERIOR {
		@Override
		public Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data) {
			final HasInterior cast = (HasInterior) obj;
			final boolean old = cast.isInterior();
			
			return(new FinalMapping<>(new Runnable() {
				@Override
				public void run() {
					cast.setInterior(Boolean.parseBoolean(data));
				}
			}, new Runnable() {
				@Override
				public void run() {
					cast.setInterior(old);
				}
			}));
		}
		
		@Override
		public String get(final Editable obj) {
			final HasInterior cast = (HasInterior) obj;
			return(String.valueOf(cast.isInterior()));
		}
		
		@Override
		public boolean isEditableByTable() {
			return(true);
		}
		
		@Override
		public Class<?> getTableClass() {
			return(Boolean.class);
		}
		
		@Override
		public String getName() {
			return("Interior");
		}
		
		@Override
		public boolean isEditableByThreeDView() {
			return(false);
		}
	};
	
	private static float[] parse3Numbers(final String data) {
		if(!data.contains(",")) {
			final float f = Float.parseFloat(data);
			return(new float[] { f, f, f });
		} else {
			final String dataFiltered = data.replace(" ", "");
			final String[] split = dataFiltered.split(",");
			
			if(split.length != 3) {
				return(null);
			}
			
			final float[] arr = new float[3];
			for(int i = 0; i < 3; i++) {
				arr[i] = Float.parseFloat(split[i]);
			}
			return(arr);
		}
	}
	private static int[] parse4Ints(final String data) {
		if(!data.contains(",")) {
			final int i = Integer.parseInt(data);
			return(new int[] { i, i, i, i });
		} else {
			final String dataFiltered = data.replace(" ", "");
			final String[] split = dataFiltered.split(",");
			
			if(split.length != 4) {
				return(null);
			}
			
			final int[] arr = new int[4];
			for(int i = 0; i < 4; i++) {
				arr[i] = Integer.parseInt(split[i]);
			}
			return(arr);
		}
	}
	
	public abstract Mapping<Runnable, Runnable> createChangeCommand(final Editable obj, final String data);
	
	public abstract String get(final Editable obj);
	
	public abstract boolean isEditableByTable();
	
	public abstract Class<?> getTableClass();
	
	public abstract String getName();
	
	public abstract boolean isEditableByThreeDView();
	
	
	public static String colorToStr(final ColorRGBA color) {
		final StringBuilder b = new StringBuilder();
		b.append((int)Math.floor(color.getRed() * 255));
		b.append(", ");
		b.append((int)Math.floor(color.getGreen() * 255));
		b.append(", ");
		b.append((int)Math.floor(color.getBlue() * 255));
		b.append(", ");
		b.append((int)Math.floor(color.getAlpha() * 255));
		return(b.toString());
	}
	public static ColorRGBA strToColor(final String str) {
		final int[] arr = parse4Ints(str);
		if(arr == null) {
			return(null);
		}
		return(new ColorRGBA(arr[0] / 255.0f, arr[1] / 255.0f, arr[2] / 255.0f, arr[3] / 255.0f));
	}
	
	public static String vecToStr(final Vector3f vec) {
		final StringBuilder b = new StringBuilder();
		b.append(vec.getX());
		b.append(", ");
		b.append(vec.getY());
		b.append(", ");
		b.append(vec.getZ());
		return(b.toString());
	}
	public static Vector3f strToVec(final String str) {
		final float[] arr = parse3Numbers(str);
		if(arr == null) {
			return(null);
		}
		
		return(new Vector3f(arr[0], arr[1], arr[2]));
	}
}
