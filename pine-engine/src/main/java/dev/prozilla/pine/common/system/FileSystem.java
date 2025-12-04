package dev.prozilla.pine.common.system;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for manipulating files and directories.
 */
public final class FileSystem {
	
	private FileSystem() {}
	
	/**
	 * Deletes a directory and its contents, if it exists.
	 * @param directory The directory to delete
	 * @return {@code true} if the directory was deleted.
	 */
	public static boolean deleteDirectoryIfExists(Path directory) {
		boolean exists = Files.exists(directory);
		if (exists) {
			deleteDirectory(directory);
		}
		return exists;
	}
	
	/**
	 * Deletes a directory and its contents.
	 * @param directory The directory to delete
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
	 * @param source The directory to copy from
	 * @param target The directory to paste into
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
	 * @param directory The directory to unwrap
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
	
	/**
	 * Downloads a URL into a target directory.
	 * @param url The URL to download from
	 * @param target The path to download to
	 */
	public static void download(String url, Path target) throws URISyntaxException, IOException {
		try (InputStream in = new URI(url).toURL().openStream()) {
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	/**
	 * Unzips a zip file into a target directory and deletes it.
	 * @param zip The zip to unzip
	 * @param target The path to unzip into
	 */
	public static void unzip(Path zip, Path target) throws IOException {
		try (ZipFile zipFile = new ZipFile(zip.toFile())) {
			zipFile.stream().forEach(entry -> {
				Path entryDestination = target.resolve(entry.getName());
				if (entry.isDirectory()) {
					try {
						Files.createDirectories(entryDestination);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				} else {
					try (InputStream in = zipFile.getInputStream(entry)) {
						Files.copy(in, entryDestination, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
		Files.delete(zip);
	}
	
	/**
	 * Creates a zip of the contents of a directory. The zip file is placed inside the same directory.
	 * @param directory The directory to create a zip of
	 * @param fileName The filename to use for the zip file
	 */
	public static void zip(Path directory, String fileName) throws IOException {
        Path zipFile = Files.createFile(directory.resolve(fileName));
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile));
                Stream<Path> files = Files.walk(directory)) {
	        files.forEach((file) -> {
				if (file.equals(zipFile) || file.equals(directory)) {
					return;
				}
				
				String entryName = PathUtils.replaceFileSeparator(directory.relativize(file).toString());
				if (Files.isDirectory(file)) {
					entryName = PathUtils.addTrailingSlash(entryName);
				}
				
		        ZipEntry entry = new ZipEntry(entryName);
		        try {
			        zos.putNextEntry(entry);
					if (!Files.isDirectory(file)) {
						Files.copy(file, zos);
					}
			        zos.closeEntry();
		        } catch (IOException e) {
			        throw new RuntimeException(e);
		        }
	        });
        }
		
    }

}
