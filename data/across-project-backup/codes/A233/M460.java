    private void loadIcon() {
        try {
            Image icon = null;
            String[] iconPaths = {"/static/favicon.ico"};

            for (String path : iconPaths) {
                if (icon != null) break;
                try {
                    try (InputStream is = getClass().getResourceAsStream(path)) {
                        if (is != null) {
                            icon = ImageIO.read(is);
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.debug("Could not load icon from " + path, e);
                }
            }

            if (icon != null) {
                frame.setIconImage(icon);
                setupTrayIcon(icon);
            } else {
                log.warn("Could not load icon from any source");
            }
        } catch (Exception e) {
            log.error("Error loading icon", e);
        }
    }
