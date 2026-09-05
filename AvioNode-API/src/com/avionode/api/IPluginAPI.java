package com.avionode.api;

import javax.swing.JPanel;

/**
 * The core plugin development interface for AvioNode.
 * All external plugins must implement this interface in order to be successfully loaded and integrated into the system.
 */
public interface IPluginAPI {
    
    /**
     * Returns the name of the plugin as it will appear in the system and user interface.
     * Example: "Unit Converter" or "Password Manager"
     * 
     * @return The plugin name
     */
    String getPluginName();
    
    /**
     * Returns the current version number of the plugin.
     * Designed for future compatibility and update checks within the AvioNode core.
     * Example: "1.0.0" or "v2.1"
     * 
     * @return The plugin version
     */
    String getVersion();
    
    /**
     * Returns the graphical panel (UI) that will be embedded into the main application's TabPane structure.
     * All visual components of the plugin (buttons, text fields, etc.) should be built inside this panel.
     * 
     * @return The JPanel instance representing the plugin's interface
     */
    JPanel getPanel();
    
    /**
     * The initialization method that gets triggered immediately when the plugin is loaded into memory by AvioNode.
     * Used for establishing database connections, initializing variables, or starting background routines.
     */
    void start();
}