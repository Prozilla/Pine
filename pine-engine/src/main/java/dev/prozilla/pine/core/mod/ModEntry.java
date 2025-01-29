package dev.prozilla.pine.core.mod;

import dev.prozilla.pine.common.Printable;

public class ModEntry implements Printable {
	
	public final Mod mod;
	public final ModMetadata metadata;
	
	public ModEntry(Mod mod, ModMetadata metadata) {
		this.mod = mod;
		this.metadata = metadata;
	}
	
	@Override
	public String toString() {
		if (metadata == null) {
			return "Unknown mod";
		} else {
			return String.format("%s v%s by %s - %s",
				metadata.getName(),
			    metadata.getVersion(),
			    metadata.getAuthor(),
			    metadata.getDescription()
			);
		}
	}
}
