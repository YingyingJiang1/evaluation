    @Override
    public boolean accept(AuditEvent event) {
        boolean accepted = true;

        if (event.getViolation() != null) {
            final String eventFileTextAbsolutePath = event.getFileName();

            if (!cachedFileAbsolutePath.equals(eventFileTextAbsolutePath)) {
                final FileText currentFileText = getFileText(eventFileTextAbsolutePath);

                if (currentFileText != null) {
                    cachedFileAbsolutePath = currentFileText.getFile().getAbsolutePath();
                    collectSuppressions(currentFileText);
                }
            }

            final Optional<Suppression> nearestSuppression =
                    getNearestSuppression(suppressions, event);
            accepted = nearestSuppression.isEmpty();
        }
        return accepted;
    }
