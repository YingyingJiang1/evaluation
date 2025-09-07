            @Override
            public void handleProgress(EnumProgress state, float percent) {
                Objects.requireNonNull(state, "state cannot be null");
                SwingUtilities.invokeLater(
                        () -> {
                            if (loadingWindow != null) {
                                switch (state) {
                                    case LOCATING:
                                        loadingWindow.setStatus("Locating Files...");
                                        loadingWindow.setProgress(0);
                                        break;
                                    case DOWNLOADING:
                                        if (percent >= 0) {
                                            loadingWindow.setStatus(
                                                    String.format(
                                                            "Downloading additional files: %.0f%%",
                                                            percent));
                                            loadingWindow.setProgress((int) percent);
                                        }
                                        break;
                                    case EXTRACTING:
                                        loadingWindow.setStatus("Extracting files...");
                                        loadingWindow.setProgress(60);
                                        break;
                                    case INITIALIZING:
                                        loadingWindow.setStatus("Initializing UI...");
                                        loadingWindow.setProgress(80);
                                        break;
                                    case INITIALIZED:
                                        loadingWindow.setStatus("Finalising startup...");
                                        loadingWindow.setProgress(90);
                                        break;
                                }
                            }
                        });
            }
