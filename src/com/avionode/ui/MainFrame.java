package com.avionode.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.avionode.engine.PluginInstaller;
import com.avionode.util.Recorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JPanel pluginBtnHolder = new JPanel();
	public static JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
					Recorder.logSystem("INFO", "MainFrame successfully initialized and displayed.");
				} catch (Exception e) {
					Recorder.logSystem("ERROR", "Critical error while launching MainFrame: " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		setPreferredSize(new Dimension(900, 600));
		setTitle("AvioNode");
		setAlwaysOnTop(false);
		setMinimumSize(new Dimension(800, 500));

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			Recorder.logSystem("ClassNotFoundException", e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			Recorder.logSystem("InstantiationException", e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Recorder.logSystem("IllegalAccessException", e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			Recorder.logSystem("UnsupportedLookAndFeelException", e.getMessage());
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1007, 604);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel sideBarPanel = new JPanel();
		sideBarPanel.setBackground(Color.RED);
		sideBarPanel.setLocation(new Point(0, 10));
		sideBarPanel.setPreferredSize(new Dimension(200, 500));
		contentPane.add(sideBarPanel, BorderLayout.WEST);
		sideBarPanel.setLayout(new BorderLayout(0, 0));

		JPanel controlBtnHolder = new JPanel();
		controlBtnHolder.setMinimumSize(new Dimension(150, 40));
		controlBtnHolder.setPreferredSize(new Dimension(200, 65));
		controlBtnHolder.setLocation(new Point(0, 10));
		sideBarPanel.add(controlBtnHolder, BorderLayout.SOUTH);
		controlBtnHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnAddNewPlugin = new JButton("Add");
		btnAddNewPlugin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnAddNewPlugin.setPreferredSize(new Dimension(80, 25));
		controlBtnHolder.add(btnAddNewPlugin);
		btnAddNewPlugin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Recorder.logSystem("INFO", "User triggered the plugin installation (Add) process.");
				PluginInstaller.addPlugin();
			}
		});

		pluginBtnHolder.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		pluginBtnHolder.setMinimumSize(new Dimension(150, 400));
		pluginBtnHolder.setLocation(new Point(0, 10));

		JLabel lblPluginsLabel = new JLabel("Plugins");
		lblPluginsLabel.setForeground(new Color(48, 48, 48));
		lblPluginsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

		JButton btnDelete = new JButton("Delete");
		btnDelete.setPreferredSize(new Dimension(80, 25));
		btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Recorder.logSystem("INFO", "Plugin removal dialog opened.");
				PluginRemovalDialog pRemovalDialog = new PluginRemovalDialog();
				pRemovalDialog.setVisible(true);
			}
		});
		controlBtnHolder.add(btnDelete);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnRefresh.setPreferredSize(new Dimension(80, 25));
		controlBtnHolder.add(btnRefresh);
		pluginBtnHolder.add(lblPluginsLabel);
		loadClass();
		btnRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Recorder.logSystem("INFO", "User triggered UI and plugin list refresh.");
				pluginBtnHolder.add(lblPluginsLabel);
				loadClass();
				refreshPaint();

			}
		});

		sideBarPanel.add(pluginBtnHolder, BorderLayout.CENTER);

		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		contentPane.add(tabbedPane, BorderLayout.CENTER);

	}

	public static void loadClass() {
		int count = 0;
		for (JButton buttonItem : PluginButtonFactory.getPluginButtonMap().values()) {
			buttonItem.setVisible(true);
			buttonItem.setPreferredSize(new Dimension(150, 25));
			pluginBtnHolder.add(buttonItem);
			pluginBtnHolder.add(Box.createVerticalStrut(5));
			count++;

		}
		Recorder.logSystem("INFO", count + " plugin buttons successfully loaded into the sidebar.");
	}

	public static void refreshPaint() {
		pluginBtnHolder.revalidate();
		pluginBtnHolder.repaint();
	}

}
