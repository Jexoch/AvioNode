package com.avionode.engine;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;

import javax.swing.JOptionPane;

import com.avionode.util.Recorder;

public class PluginRemover {
	public static void removePlugin(String selectedItem) {
		Recorder.logSystem("INFO", "Initiating removal process for plugin: " + selectedItem);
		URLClassLoader loader = PluginLoader.pluginClassLoaderMap.get(selectedItem);
		File file = PluginLoader.pluginFileMap.get(selectedItem);
		try {
			if (loader != null) {
				try {
					loader.close();
					Recorder.logSystem("INFO", "Classloader closed successfully for plugin: " + selectedItem);
				} catch (IOException e) {
					Recorder.logSystem("IOException", e.getMessage() + " | Cause : " + e.getCause());
					e.printStackTrace();
				}
			}

			if (file.exists() && file != null) {
				try {
					Files.delete(file.toPath());
					Recorder.logSystem("INFO", "Physical plugin file deleted successfully: " + file.getName());
					JOptionPane.showMessageDialog(null, "Deletion successfully", "INFO",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					Recorder.logSystem("IOException", e.getMessage() + " | Cause : " + e.getCause());
					e.printStackTrace();
				}
				PluginLoader.pluginClassLoaderMap.remove(selectedItem);
				PluginLoader.pluginFileMap.remove(selectedItem);
			}
		} catch (Exception e) {
			Recorder.logSystem("IOException", e.getMessage() + " | Cause : " + e.getCause());
			JOptionPane.showMessageDialog(null, "A error while file find process" + e.getMessage(), "error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
