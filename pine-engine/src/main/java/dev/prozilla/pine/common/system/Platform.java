package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.util.ArrayUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents the platforms supported by Pine.
 *
 * <p>This is a wrapper for {@link org.lwjgl.system.Platform}.</p>
 */
public enum Platform {
	FREEBSD(org.lwjgl.system.Platform.FREEBSD) {
		@Override
		public String getUserPath(String userHome, String separator) {
			return LINUX.getUserPath(userHome, separator);
		}
	},
	LINUX(org.lwjgl.system.Platform.LINUX) {
		@Override
		public String getUserPath(String userHome, String separator) {
			String xdgData = System.getenv("XDG_DATA_HOME");
			if (xdgData == null) {
				return String.format("%s.local%sshare", userHome, separator);
			} else {
				return PathUtils.replaceFileSeparator(xdgData, separator);
			}
		}
	},
	MACOS(org.lwjgl.system.Platform.MACOSX) {
		@Override
		public String getUserPath(String userHome, String separator) {
			return String.format("%sLibrary%sApplication Support", userHome, separator);
		}
	},
	WINDOWS(org.lwjgl.system.Platform.WINDOWS) {
		@Override
		public String getUserPath(String userHome, String separator) {
			String userDirectory = System.getenv("USERPROFILE");
			if (userDirectory == null) {
				userDirectory = userHome;
			}
			return String.format("%sAppData%sLocalLow", PathUtils.addTrailingSlash(PathUtils.replaceFileSeparator(userDirectory, separator)), separator);
		}
	};
	
	private final org.lwjgl.system.Platform lwjglPlatform;
	
	private static final Platform current = fromLwjglPlatform(org.lwjgl.system.Platform.get());
	
	Platform(org.lwjgl.system.Platform lwjglPlatform) {
		this.lwjglPlatform = lwjglPlatform;
	}
	
	/**
	 * Returns the identifier of this platform
	 * @return The identifier of this platform
	 */
	public String getIdentifier() {
		return getName().toLowerCase();
	}
	
	/**
	 * Returns the name of this platform
	 * @return The name of this platform
	 */
	public String getName() {
		return lwjglPlatform.getName();
	}
	
	/**
	 * Checks if this is the current platform.
	 * @return {@code true} if this is the current platform.
	 */
	public boolean isCurrent() {
		return this == current;
	}
	
	public abstract String getUserPath(String userHome, String separator);
	
	public static String getPersistentDataPath(String subDirectory) {
		return getPersistentDataPath(subDirectory, false);
	}
	
	public static String getPersistentDataPath(String subDirectory, boolean createDirectories) {
		String userHome = PathUtils.addTrailingSlash(PathUtils.replaceFileSeparator(System.getProperty("user.home")));
		
		String userPath;
		if (current != null) {
			userPath = PathUtils.addTrailingSlash(current.getUserPath(userHome, "/"));
		} else {
			userPath = userHome;
		}
		
		Path persistentDataPath = Paths.get(userPath, subDirectory);
		if (createDirectories) {
			try {
				Files.createDirectories(persistentDataPath);
			} catch (Exception e) {
				throw new RuntimeException("Failed to create persistent data directory: " + persistentDataPath, e);
			}
		}
		
		return PathUtils.addTrailingSlash(PathUtils.replaceFileSeparator(persistentDataPath.toAbsolutePath().toString()));
	}
	
	/**
	 * Returns the platform Pine is running on.
	 * @return The current platform, or {@code null} if the platform is not supported.
	 */
	public static Platform get() {
		return current;
	}
	
	/**
	 * Returns the architecture Pine is running on.
	 * @return The current architecture, or {@code null} if the architecture is not supported.
	 */
	public static Architecture getArchitecture() {
		return Architecture.current;
	}
	
	/**
	 * Returns the descriptor of the platform and architecture in the format {@code platform/architecture}.
	 * @return The descriptor of the platform and architecture.
	 */
	public static String getDescriptor() {
		StringBuilder stringBuilder = new StringBuilder();
		if (current != null) {
			stringBuilder.append(current.getName());
		}
		stringBuilder.append("/");
		if (Architecture.current != null) {
			stringBuilder.append(Architecture.current);
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Checks if the current platform is Unix-based.
	 * @return {@code true} if the current platform is Unix-based.
	 */
	public static boolean isUnix() {
		return current == LINUX || current == FREEBSD || current == MACOS;
	}
	
	/**
	 * Checks if the current platform is supported by Pine.
	 * @return {@code true} if the current platform is supported by Pine.
	 */
	public static boolean isSupported() {
		return current != null;
	}
	
	private static Platform fromLwjglPlatform(org.lwjgl.system.Platform lwjglPlatform) {
		for (Platform platform : values()) {
			if (platform.lwjglPlatform == lwjglPlatform) {
				return platform;
			}
		}
		return null;
	}
	
	public static String mapLibraryNameBundled(String name) {
		return org.lwjgl.system.Platform.mapLibraryNameBundled(name);
	}
	
	public static Platform parse(String input) {
		return ArrayUtils.findByString(values(), input, true);
	}
	
	public static Architecture parseArchitecture(String input) {
		return ArrayUtils.findByString(Architecture.values(), input, true);
	}
	
	/**
	 * Represents the architectures supported by Pine.
	 *
	 * <p>This is a wrapper for {@link org.lwjgl.system.Platform.Architecture}</p>
	 */
	public enum Architecture {
		X64(org.lwjgl.system.Platform.Architecture.X64, true),
		X86(org.lwjgl.system.Platform.Architecture.X86, false),
		ARM64(org.lwjgl.system.Platform.Architecture.ARM64, true),
		ARM32(org.lwjgl.system.Platform.Architecture.ARM32, false),
		PPC64LE(org.lwjgl.system.Platform.Architecture.PPC64LE, true),
		RISCV64(org.lwjgl.system.Platform.Architecture.RISCV64, true);
		
		private final org.lwjgl.system.Platform.Architecture lwjglArchitecture;
		private final boolean is64Bit;
		
		private static final Architecture current = fromLwjglArchitecture(org.lwjgl.system.Platform.getArchitecture());
		
		Architecture(org.lwjgl.system.Platform.Architecture lwjglArchitecture, boolean is64Bit) {
			this.lwjglArchitecture = lwjglArchitecture;
			this.is64Bit = is64Bit;
		}
		
		/**
		 * Checks if this is the current architecture.
		 * @return {@code true} if this is the current architecture.
		 */
		public boolean isCurrent() {
			return this == current;
		}
		
		/**
		 * Checks if the current architecture is 64-bit.
		 * @return {@code true} if the current architecture is 64-bit.
		 */
		public static boolean is64Bit() {
			return current != null && current.is64Bit;
		}
		
		/**
		 * Checks if the current architecture is Arm-based.
		 * @return {@code true} if the current architecture is Arm-based.
		 */
		public static boolean isArm() {
			return current == ARM32 || current == ARM64;
		}
		
		/**
		 * Checks if the current architecture is supported by Pine.
		 * @return {@code true} if the current architecture is supported by Pine.
		 */
		public static boolean isSupported() {
			return current != null;
		}
		
		private static Architecture fromLwjglArchitecture(org.lwjgl.system.Platform.Architecture lwjglArchitecture) {
			for (Architecture architecture : values()) {
				if (architecture.lwjglArchitecture == lwjglArchitecture) {
					return architecture;
				}
			}
			return null;
		}
		
	}
	
}
