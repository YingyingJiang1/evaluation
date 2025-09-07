    private void log() {
        final String folder = getStringOrDefault(folderPattern, "");
        final String fileName = getStringOrDefault(fileNamePattern, "");

        if (match) {
            log(1, MSG_MATCH, folder, fileName);
        }
        else {
            log(1, MSG_MISMATCH, folder, fileName);
        }
    }
