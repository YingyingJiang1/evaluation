    private static MatchResult matchHeader(FileText fileText, HeaderFileMetadata headerFile) {
        final int fileSize = fileText.size();
        final List<Pattern> headerPatterns = headerFile.getHeaderPatterns();
        final int headerPatternSize = headerPatterns.size();

        int mismatchLine = MISMATCH_CODE;
        int index;
        for (index = 0; index < headerPatternSize && index < fileSize; index++) {
            if (!headerPatterns.get(index).matcher(fileText.get(index)).find()) {
                mismatchLine = index;
                break;
            }
        }
        if (index < headerPatternSize) {
            mismatchLine = index;
        }

        final MatchResult matchResult;
        if (mismatchLine == MISMATCH_CODE) {
            matchResult = MatchResult.matching();
        }
        else {
            matchResult = createMismatchResult(headerFile, fileText, mismatchLine);
        }
        return matchResult;
    }
