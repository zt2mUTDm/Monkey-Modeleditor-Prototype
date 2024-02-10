package core;

public enum CollisionGroup {
	GROUP_0,
	GROUP_1,
	GROUP_2,
	GROUP_3,
	GROUP_4,
	GROUP_5,
	GROUP_6,
	GROUP_7,
	GROUP_8,
	GROUP_9,
	GROUP_10,
	GROUP_11,
	GROUP_12,
	GROUP_13,
	GROUP_14,
	GROUP_15;
	
	@Override
	public String toString() {
		final String name = name();
		final String s = name.replace('_', ' ').substring(1);
		return(name.charAt(0) + s.toLowerCase());
	}
}
