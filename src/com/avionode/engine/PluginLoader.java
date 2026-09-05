package com.avionode.engine;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.avionode.api.IPluginAPI;
import com.avionode.util.Recorder;

public class PluginLoader {
	public static String userHome = System.getProperty("user.home");
	public static Path pluginPath = Paths.get(userHome, ".AvioNode", "Content", "Plugins");
	public static HashMap<String, File> pluginFileMap = new HashMap<String, File>();
	public static HashMap<String, URLClassLoader> pluginClassLoaderMap = new HashMap<String, URLClassLoader>();

	public static ArrayList<IPluginAPI> getLoadedClasses() {
		ArrayList<IPluginAPI> aList = new ArrayList<IPluginAPI>();

		File pluginFile = new File(pluginPath.toString());
		for (URLClassLoader oldClassLoader : pluginClassLoaderMap.values()) {
			try {
				oldClassLoader.close();
			} catch (IOException e) {
				Recorder.logSystem("IOException", e.getMessage());
				e.printStackTrace();
			}
		}
		pluginClassLoaderMap.clear();
		pluginFileMap.clear();

		if (!pluginFile.isDirectory() || !pluginFile.exists()) {
			try {
				Files.createDirectories(pluginPath);
				Recorder.logSystem("INFO", "Plugin directory created successfully at: " + pluginPath);
			} catch (IOException e) {
				Recorder.logSystem("IOException", e.getMessage());
				e.printStackTrace();
				return aList;
			}

		}

		File[] filesArray = pluginFile.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});

		if (filesArray != null) {
			for (File file : filesArray) {

				try {
					URL pluginURL = file.toURI().toURL();
					URL[] urls = { pluginURL };
					URLClassLoader uClassLoader = URLClassLoader.newInstance(urls);
					try (JarFile jFile = new JarFile(file)) {

						Enumeration<JarEntry> enumeration = jFile.entries();
						while (enumeration.hasMoreElements()) {
							JarEntry jarEntry = (JarEntry) enumeration.nextElement();

							if (jarEntry.getName().endsWith(".class")) {
								String corClassName = jarEntry.getName().replace("/", ".").replace(".class", "");

								try {
									Class<?> loadedClass = uClassLoader.loadClass(corClassName);

									if (IPluginAPI.class.isAssignableFrom(loadedClass) && !loadedClass.isInterface()) {
										try {
											Object object = loadedClass.getDeclaredConstructor().newInstance();
											IPluginAPI aPluginAPI = (IPluginAPI) object;

											aList.add(aPluginAPI);
											Recorder.logSystem("INFO",
													"Plugin successfully loaded: '" + aPluginAPI.getPluginName()
															+ "' (v" + aPluginAPI.getVersion() + ")");
											pluginClassLoaderMap.put(aPluginAPI.getPluginName(), uClassLoader);
											pluginFileMap.put(aPluginAPI.getPluginName(), file);

										} catch (InstantiationException e) {
											Recorder.logSystem("InstantiationException", e.getMessage());
											e.printStackTrace();
										} catch (IllegalAccessException e) {
											Recorder.logSystem("IllegalAccessException", e.getMessage());
											e.printStackTrace();
										} catch (IllegalArgumentException e) {
											Recorder.logSystem("IllegalArgumentException", e.getMessage());
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											Recorder.logSystem("InvocationTargetException", e.getMessage());
											e.printStackTrace();
										} catch (NoSuchMethodException e) {
											Recorder.logSystem("NoSuchMethodException", e.getMessage());
											e.printStackTrace();
										}
									}

								} catch (ClassNotFoundException e) {
									Recorder.logSystem("ClassNotFoundException", e.getMessage());
									e.printStackTrace();
								}

							}

						}

					} catch (IOException e) {
						Recorder.logSystem("IOException", e.getMessage());
						e.printStackTrace();
					}

				} catch (MalformedURLException e) {
					Recorder.logSystem("MalformedURLException", e.getMessage());
					e.printStackTrace();
				}

			}
		} else {
			Recorder.logSystem("WARNING", "Plugin directory is empty: " + pluginFile.getPath());
		}

		return aList;
	}

}
