package io.nobt.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileReader {

	public static String readFromResource(String pathToResource) {
		try {
			final byte[] bytes = Files.readAllBytes(Paths.get(JsonFileReader.class.getResource(pathToResource).toURI()));
			return new String(bytes, "UTF-8");
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
