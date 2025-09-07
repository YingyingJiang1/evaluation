    private void collectSuppressions(FileText fileText) {
        suppressions.clear();

        for (int lineNo = 0; lineNo < fileText.size(); lineNo++) {
            final Suppression suppression = getSuppression(fileText, lineNo);
            if (suppression != null) {
                suppressions.add(suppression);
            }
        }
    }
