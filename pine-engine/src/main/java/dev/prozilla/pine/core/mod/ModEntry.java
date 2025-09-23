package dev.prozilla.pine.core.mod;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.lifecycle.Destructible;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a modification (mod) loaded by the {@link ModManager}.
 * Serves as a container for mod functionality and metadata.
 */
public class ModEntry implements Printable, Destructible {
	
	public final Mod mod;
	public final ModMetadata metadata;
	
	public ModEntry(Mod mod, ModMetadata metadata) {
		this.mod = mod;
		this.metadata = metadata;
	}
	
	@Override
	public void destroy() {
		mod.destroy();
	}
	
	/**
	 * @return The string representation of this mod based on its metadata or <code>"Unknown mod"</code> if the metadata is missing.
	 */
	@Override
	public @NotNull String toString() {
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
