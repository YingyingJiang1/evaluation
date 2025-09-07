    @Override
    protected void processFiltered(File file, FileText fileText) {
        if (!headerFilesMetadata.isEmpty()) {
            final List<MatchResult> matchResult = headerFilesMetadata.stream()
                    .map(headerFile -> matchHeader(fileText, headerFile))
                    .collect(Collectors.toUnmodifiableList());

            if (matchResult.stream().noneMatch(match -> match.isMatching)) {
                final MatchResult mismatch = matchResult.get(0);
                final String allConfiguredHeaderPaths = getConfiguredHeaderPaths();
                log(mismatch.lineNumber, mismatch.messageKey,
                        mismatch.messageArg, allConfiguredHeaderPaths);
            }
        }
    }
