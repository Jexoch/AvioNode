package com.avionode.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.avionode.api.IPluginAPI;
import com.avionode.engine.PluginLoader;
import com.avionode.engine.PluginRemover;
import com.avionode.util.Recorder;

public class PluginRemovalDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JCheckBox chckBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PluginRemovalDialog dialog = new PluginRemovalDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PluginRemovalDialog() {
		Recorder.logSystem("INFO", "Plugin removal dialog initialized."); // <-- BU SATIRI EKLE
		ArrayList<IPluginAPI> alist = PluginLoader.getLoadedClasses();
		ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			for (IPluginAPI apiItem : alist) {
				chckBox = new JCheckBox(apiItem.getPluginName());
				checkBoxList.add(chckBox);
				contentPanel.add(chckBox);

			}
		}
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPanel.add(chckBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Delete selected plugins");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						for (JCheckBox jCheckBox : checkBoxList) {
							if (jCheckBox.isSelected()) {
								int response = JOptionPane.showConfirmDialog(null,
										"Selected items will be deleted. Are you sure?", "Confirm Deletion",
										JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
								if (response == JOptionPane.YES_OPTION) {
									Recorder.logSystem("INFO",
											"User confirmed deletion for plugin: " + jCheckBox.getText());
									PluginRemover.removePlugin(jCheckBox.getText());
									dispose();
								} else {
									Recorder.logSystem("INFO",
											"User aborted deletion for plugin: " + jCheckBox.getText());
									JOptionPane.showMessageDialog(null, "Deletion canceled", "Information Message",
											JOptionPane.INFORMATION_MESSAGE);
								}

							} else {
								dispose();
							}

						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");

				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Recorder.logSystem("INFO", "Plugin removal dialog closed via Cancel button.");
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		setLocationRelativeTo(null);
		setModal(true);
	}

}
