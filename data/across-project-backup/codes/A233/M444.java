    public void setProgress(final int progress) {
        SwingUtilities.invokeLater(
                () -> {
                    try {
                        int validProgress = Math.min(Math.max(progress, 0), 100);
                        log.info(
                                "Setting progress to {}% at {}ms since start",
                                validProgress, System.currentTimeMillis() - startTime);

                        // Log additional details when near 90%
                        if (validProgress >= 85 && validProgress <= 95) {
                            log.info(
                                    "Near 90% progress - Current status: {}, Window visible: {}, "
                                            + "Progress bar responding: {}, Memory usage: {}MB",
                                    statusLabel.getText(),
                                    isVisible(),
                                    progressBar.isEnabled(),
                                    Runtime.getRuntime().totalMemory() / (1024 * 1024));

                            // Add thread state logging
                            Thread currentThread = Thread.currentThread();
                            log.info(
                                    "Current thread state - Name: {}, State: {}, Priority: {}",
                                    currentThread.getName(),
                                    currentThread.getState(),
                                    currentThread.getPriority());

                            if (validProgress >= 90 && validProgress < 95) {
                                checkAndRefreshExplorer();
                            } else {
                                // Reset the timer if we move past 95%
                                if (validProgress >= 95) {
                                    if (stuckTimer != null) {
                                        stuckTimer.stop();
                                    }
                                    timeAt90Percent = -1;
                                }
                            }
                        }

                        progressBar.setValue(validProgress);
                        progressBar.setString(validProgress + "%");
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } catch (Exception e) {
                        log.error("Error updating progress to " + progress, e);
                    }
                });
    }
