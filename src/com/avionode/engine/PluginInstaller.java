package com.avionode.engine;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.avionode.util.Recorder;

public class PluginInstaller {
	public static void addPlugin() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose jar file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setApproveButtonText("Select");

		if (!Files.exists(PluginLoader.pluginPath)) {
			try {
				Files.createDirectories(PluginLoader.pluginPath);
				Recorder.logSystem("INFO", "Plugin directory created successfully at: " + PluginLoader.pluginPath);
			} catch (IOException e) {
				Recorder.logSystem("IOException", e.getMessage() + " | Cause : " + e.getCause());
				e.printStackTrace();
			}
		}

		if (fileChooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
			if (Files.isDirectory(fileChooser.getSelectedFile().toPath())) {
				Recorder.logSystem("INFO",
						"Directory-based plugin installation started for: " + fileChooser.getSelectedFile().getName());
				try {
					Files.walkFileTree(fileChooser.getSelectedFile().toPath(), new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
								throws IOException {
							Path temPath = PluginLoader.pluginPath
									.resolve(fileChooser.getSelectedFile().toPath().relativize(dir));
							Files.createDirectories(temPath);
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							Path temPath = PluginLoader.pluginPath
									.resolve(fileChooser.getSelectedFile().toPath().relativize(file));
							Files.copy(file, temPath, StandardCopyOption.REPLACE_EXISTING);
							return FileVisitResult.CONTINUE;
						}

					});
				} catch (IOException e) {
					Recorder.logSystem("IOException", e.getMessage() + " | Cause : " + e.getCause());
					e.printStackTrace();
				}
			} else {
				try {
					Files.copy(fileChooser.getSelectedFile().toPath(),
							PluginLoader.pluginPath.resolve(fileChooser.getSelectedFile().toPath().getFileName()),
							StandardCopyOption.REPLACE_EXISTING);
					Recorder.logSystem("INFO",
							"Plugin successfully installed: " + fileChooser.getSelectedFile().toPath().getFileName());
				} catch (IOException e) {
					Recorder.logSystem("IOException", e.getMessage() + " | Cause : " + e.getCause());
					e.printStackTrace();
				}
			}
		}

	}
}
