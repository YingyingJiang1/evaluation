    private void setupTrayIcon(Image icon) {
        if (!SystemTray.isSupported()) {
            log.warn("System tray is not supported");
            return;
        }

        try {
            systemTray = SystemTray.getSystemTray();

            // Create popup menu
            PopupMenu popup = new PopupMenu();

            // Create menu items
            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(
                    e -> {
                        frame.setVisible(true);
                        frame.setState(Frame.NORMAL);
                    });

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(
                    e -> {
                        cleanup();
                        System.exit(0);
                    });

            // Add menu items to popup menu
            popup.add(showItem);
            popup.addSeparator();
            popup.add(exitItem);

            // Create tray icon
            trayIcon = new TrayIcon(icon, "Stirling-PDF", popup);
            trayIcon.setImageAutoSize(true);

            // Add double-click behavior
            trayIcon.addActionListener(
                    e -> {
                        frame.setVisible(true);
                        frame.setState(Frame.NORMAL);
                    });

            // Add tray icon to system tray
            systemTray.add(trayIcon);

            // Modify frame behavior to minimize to tray
            frame.addWindowStateListener(
                    new WindowStateListener() {
                        public void windowStateChanged(WindowEvent e) {
                            if (e.getNewState() == Frame.ICONIFIED) {
                                frame.setVisible(false);
                            }
                        }
                    });

        } catch (AWTException e) {
            log.error("Error setting up system tray icon", e);
        }
    }
