    private static SortedSet<AbstractCheck> createNewCheckSortedSet() {
        return new TreeSet<>(
                Comparator.<AbstractCheck, String>comparing(check -> check.getClass().getName())
                        .thenComparing(AbstractCheck::getId,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparingInt(AbstractCheck::hashCode));
    }
