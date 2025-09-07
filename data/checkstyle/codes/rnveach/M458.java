    private boolean isMatchFolder(String folderPath) {
        final boolean result;

        // null pattern always matches, regardless of value of 'match'
        if (folderPattern == null) {
            result = true;
        }
        else {
            // null pattern means 'match' applies to the folderPattern matching
            final boolean useMatch = fileNamePattern != null || match;
            result = folderPattern.matcher(folderPath).find() == useMatch;
        }

        return result;
    }
