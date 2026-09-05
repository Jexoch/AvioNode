# AvioNode 

**AvioNode** is a modular, lightweight, and extensible Java-based plugin framework. It allows developers to load, manage, and interact with external `.jar` plugins dynamically at runtime using `URLClassLoader` and Java Reflection.

---
## Note
When deleting the application, do not forget to delete the `.AvioNode` folder from your `user.home` directory (`C:\Users\YourUsername` on Windows) to ensure a clean removal.


## Features

* **Dynamic Plugin Loading:** Load and unload `.jar` plugins on the fly without restarting the main application.
* **Modular Architecture:** Strict adherence to the `IPluginAPI` contract ensures seamless integration of third-party modules.
* **Robust File & Log Management:** Automatically creates and maintains localized workspace directories (`~/.AvioNode/Content/Plugins` and system logs) via a centralized `Recorder` utility.
* **Safe Uninstallation:** Safely closes active classloaders and deletes physical plugin files upon removal.

---

## Tech Stack

* **Language:** Java
* **UI Framework:** Java Swing / AWT
* **Core Concepts:** Reflection, `URLClassLoader`, NIO.2 File API

---

## Project Structure

```text
AvioNode/
│
├── src/
│   └── com/avionode/
│       ├── engine/       # Core systems (PluginLoader, PluginInstaller, PluginRemover)
│       ├── ui/           # User interface components (MainFrame, PluginButtonFactory, etc.)
│       └── util/         # Utilities (Recorder for system logs)
│
├── lib/
│   └── API/
│       └── AvioNode-API.jar  # External API contract library required for plugins
│
├── .gitignore
├── LICENSE
└── README.md
