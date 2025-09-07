    private Suppression getSuppression(FileText fileText, int lineNo) {
        final String line = fileText.get(lineNo);
        final Matcher nearbyTextMatcher = nearbyTextPattern.matcher(line);

        Suppression suppression = null;
        if (nearbyTextMatcher.find()) {
            final String text = nearbyTextMatcher.group(0);
            suppression = new Suppression(text, lineNo + 1, this);
        }

        return suppression;
    }
