    public void setStatus(final String status) {
        log.info(
                "Status update at {}ms - Setting status to: {}",
                System.currentTimeMillis() - startTime,
                status);

        SwingUtilities.invokeLater(
                () -> {
                    try {
                        String validStatus = status != null ? status : "";
                        statusLabel.setText(validStatus);

                        // Log UI state when status changes
                        log.info(
                                "UI State - Window visible: {}, Progress: {}%, Status: {}",
                                isVisible(), progressBar.getValue(), validStatus);

                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } catch (Exception e) {
                        log.error("Error updating status to: " + status, e);
                    }
                });
    }
