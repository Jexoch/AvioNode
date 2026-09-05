package com.avionode.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.avionode.api.IPluginAPI;
import com.avionode.engine.PluginLoader;
import com.avionode.util.Recorder;

public class PluginButtonFactory {
	public static TreeMap<String, JButton> getPluginButtonMap() {
		ArrayList<IPluginAPI> acArrayList = PluginLoader.getLoadedClasses();
		TreeMap<String, JButton> pluginMap = new TreeMap<String, JButton>();

		for (IPluginAPI apiClass : acArrayList) {
			JButton plugButton = new JButton(apiClass.getPluginName());
			plugButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					apiClass.start();
					Recorder.logSystem("INFO", "Plugin triggered: '" + apiClass.getPluginName() + "' (Version: "
							+ apiClass.getVersion() + ")");
					MainFrame.tabbedPane.addTab(null, apiClass.getPanel());
					int tabIndex = MainFrame.tabbedPane.getTabCount() - 1;

					JPanel panel = new JPanel();
					panel.setOpaque(false);

					JLabel lbJLabel = new JLabel(apiClass.getPluginName());
					JButton cBtn = new JButton("X");
					cBtn.setPreferredSize(new Dimension(17, 17));
					cBtn.setMargin(new Insets(0, 0, 0, 0));
					cBtn.setFocusable(false);
					cBtn.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							int tabIndexHolder = MainFrame.tabbedPane.indexOfTabComponent(panel);
							if (tabIndexHolder != -1) {
								Recorder.logSystem("INFO", "Closing tab for plugin: " + apiClass.getPluginName());
								MainFrame.tabbedPane.remove(tabIndexHolder);
							}
						}
					});

					panel.add(lbJLabel);
					panel.add(cBtn);

					MainFrame.tabbedPane.setTabComponentAt(tabIndex, panel);
					MainFrame.tabbedPane.setSelectedIndex(tabIndex);
					Recorder.logSystem("INFO",
							"Tab successfully created and focused for plugin: " + apiClass.getPluginName());
				}
			});
			pluginMap.put(apiClass.getPluginName(), plugButton);
		}
		Recorder.logSystem("INFO", pluginMap.size() + " plugin buttons mapped successfully.");
		return pluginMap;
	}

}
