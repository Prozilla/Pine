package dev.prozilla.pine.core.component;

public enum ComponentQuery {
	/** Only checks the first level. */
	SHALLOW(Mode.SHALLOW, false),
	/** Checks each level until a level contains a match. */
	NEAREST_LEVEL(Mode.NEAREST_LEVEL, false),
	/** Checks each path until a match is encountered along that path. */
	NEAREST_PATHS(Mode.NEAREST_PATHS, false),
	/** Checks all components, excluding those of the entity this query is executed on. */
	EXHAUSTIVE(Mode.EXHAUSTIVE, false),
	/** Checks the entity this query is executed on first, then the first level. */
	SELF_OR_SHALLOW(Mode.SHALLOW, true),
	/** Checks the entity this query is executed on first, then each level until a level contains a match. */
	SELF_OR_NEAREST_LEVEL(Mode.NEAREST_LEVEL, true),
	/** Checks the entity this query is executed on first, then each path until a match is encountered along that path. */
	SELF_OR_NEAREST_PATHS(Mode.NEAREST_PATHS, true),
	/** Checks all components, including those of the entity this query is executed on. */
	EXHAUSTIVE_WITH_SELF(Mode.EXHAUSTIVE, true);
	
	/** Determines the limits of the search. */
	private final Mode mode;
	/** Determines whether the components of the entity this query is executed on are included in the search. */
	private final boolean includeSelf;
	
	ComponentQuery(Mode mode, boolean includeSelf) {
		this.mode = mode;
		this.includeSelf = includeSelf;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public boolean includesSelf() {
		return includeSelf;
	}
	
	public boolean isRecursive() {
		return mode != Mode.SHALLOW;
	}
	
	public ComponentQuery withMode(Mode mode) {
		if (this.mode == mode) {
			return this;
		}
		return find(mode, this.includeSelf);
	}
	
	public ComponentQuery withSelf() {
		if (includeSelf) {
			return this;
		}
		return find(mode, true);
	}
	
	public ComponentQuery withoutSelf() {
		if (!includeSelf) {
			return this;
		}
		return find(mode, false);
	}
	
	private static ComponentQuery find(Mode mode, boolean includeSelf) {
		for (ComponentQuery query : values()) {
			if (query.mode == mode && query.includeSelf == includeSelf) {
				return query;
			}
		}
		return null;
	}
	
	public enum Mode {
		/** Only includes components in the first level. */
		SHALLOW,
		/** Only includes components in the nearest level that contains a match. */
		NEAREST_LEVEL,
		/** Only includes the first component in each path. */
		NEAREST_PATHS,
		/** Includes all components. */
		EXHAUSTIVE;
	}
	
}