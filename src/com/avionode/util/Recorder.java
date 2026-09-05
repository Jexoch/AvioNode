package com.avionode.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Recorder {
	private static String userHome = System.getProperty("user.home");
	private static Path logDirPath = Paths.get(userHome, ".AvioNode", "logs");
	private static Path logFilePath = logDirPath.resolve("avionode.log");

	public static void logSystem(String type, String description) {
		String date = LocalDate.now().toString();
		String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

		String logMessage = String.format("[%s %s] [%s] %s%n", date, time, type, description);
		System.out.print(logMessage);
		writeToDisk(logMessage);

	}

	private static void writeToDisk(String log) {
		if (!Files.exists(logDirPath)) {
			try {
				Files.createDirectories(logDirPath);
			} catch (IOException e) {
				System.err.println("CRITICAL: Log file didn't create " + e.getMessage());
				e.printStackTrace();
			}
		}
		try (BufferedWriter bWriter = Files.newBufferedWriter(logFilePath, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			bWriter.write(log);

		} catch (Exception e) {
			System.err.println("CRITICAL: Logs didn't write " + e.getMessage());
		}
	}

}
