    private static Suppression getNearestSuppression(Collection<Suppression> suppressions,
                                                     AuditEvent event) {
        return suppressions
            .stream()
            .filter(suppression -> suppression.isMatch(event))
            .reduce((first, second) -> second)
            .filter(suppression -> suppression.suppressionType != SuppressionType.ON)
            .orElse(null);
    }
