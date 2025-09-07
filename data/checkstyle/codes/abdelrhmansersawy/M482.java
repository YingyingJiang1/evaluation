    private static MatchResult createMismatchResult(HeaderFileMetadata headerFile,
                                                    FileText fileText, int mismatchLine) {
        final String messageKey;
        final int lineToLog;
        final String messageArg;

        if (headerFile.getHeaderPatterns().size() > fileText.size()) {
            messageKey = MSG_HEADER_MISSING;
            lineToLog = 1;
            messageArg = headerFile.getHeaderFilePath();
        }
        else {
            messageKey = MSG_HEADER_MISMATCH;
            lineToLog = mismatchLine + 1;
            final String lineContent = headerFile.getLineContents().get(mismatchLine);
            if (lineContent.isEmpty()) {
                messageArg = EMPTY_LINE_PATTERN;
            }
            else {
                messageArg = lineContent;
            }
        }
        return MatchResult.mismatch(lineToLog, messageKey, messageArg);
    }
