    private static Optional<Suppression> getNearestSuppression(Collection<Suppression> suppressions,
                                                               AuditEvent event) {
        return suppressions
                .stream()
                .filter(suppression -> suppression.isMatch(event))
                .findFirst();
    }
