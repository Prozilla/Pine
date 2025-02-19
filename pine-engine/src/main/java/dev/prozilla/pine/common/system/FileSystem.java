package dev.prozilla.pine.common.system;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class for manipulating files and directories.
 */
public final class FileSystem {
	
	/**
	 * Helper method for deleting non-empty directories.
	 */
	public static void deleteDirectory(Path directory) {
		try (Stream<Path> pathStream = Files.walk(directory)) {
			pathStream.sorted(Comparator.reverseOrder())
			 .map(Path::toFile)
			 .forEach(File::delete);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Helper method for copying a directory and its contents recursively.
	 */
	public static void copyDirectory(File source, File target) throws IOException {
		if (source.isDirectory()) {
			if (!target.exists()) {
				target.mkdir();
			}
			
			// List all files and directories
			String[] files = source.list();
			if (files != null) {
				for (String file : files) {
					copyDirectory(new File(source, file), new File(target, file));
				}
			}
		} else {
			// If it's a file, copy it
			try (InputStream in = new FileInputStream(source);
			     OutputStream out = new FileOutputStream(target)) {
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
			}
		}
	}
	
	public static Path getSubdirectory(Path directory) throws IOException {
		try (Stream<Path> stream = Files.list(directory)) {
			Optional<Path> subDirectoryPath = stream.filter(Files::isDirectory).findFirst();
			if (subDirectoryPath.isPresent()) {
				return subDirectoryPath.get();
			}
		}
		
		return null;
	}
	
	/**
	 * Moves the contents of a directory one level up and deletes the directory.
	 */
	public static void unwrapDirectory(Path directory) throws IOException {
		Path parent = directory.getParent();
		
		// Move contents one level up
		try (Stream<Path> files = Files.list(directory)) {
			files.forEach((src) -> {
				try {
					Files.move(src, parent.resolve(src.getFileName()), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
		
		Files.delete(directory);
	}
	
}
