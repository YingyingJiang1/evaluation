    private Optional<Suppression> getSuppression(FileText fileText, int lineNo) {
        final String line = fileText.get(lineNo);
        final Matcher onCommentMatcher = onCommentFormat.matcher(line);
        final Matcher offCommentMatcher = offCommentFormat.matcher(line);

        Suppression suppression = null;
        if (onCommentMatcher.find()) {
            suppression = new Suppression(onCommentMatcher.group(0),
                lineNo + 1, SuppressionType.ON, this);
        }
        if (offCommentMatcher.find()) {
            suppression = new Suppression(offCommentMatcher.group(0),
                lineNo + 1, SuppressionType.OFF, this);
        }

        return Optional.ofNullable(suppression);
    }
