        public static HeaderFileMetadata createFromFile(String headerPath) {
            if (CommonUtil.isBlank(headerPath)) {
                throw new IllegalArgumentException("Header file is not set");
            }
            try {
                final URI uri = CommonUtil.getUriByFilename(headerPath);
                final List<String> readerLines = getLines(headerPath, uri);
                final List<Pattern> patterns = readerLines.stream()
                        .map(HeaderFileMetadata::createPatternFromLine)
                        .collect(Collectors.toUnmodifiableList());
                return new HeaderFileMetadata(uri, headerPath, patterns, readerLines);
            }
            catch (CheckstyleException exc) {
                throw new IllegalArgumentException(
                        "Error reading or corrupted header file: " + headerPath, exc);
            }
        }
