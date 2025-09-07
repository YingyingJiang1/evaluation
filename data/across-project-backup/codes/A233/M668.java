    private void fixedRepeatOverlay(
            Map<Integer, String> overlayGuide, File[] overlayFiles, int[] counts, int basePageCount)
            throws IOException {
        if (overlayFiles.length != counts.length) {
            throw new IllegalArgumentException(
                    "Counts array length must match the number of overlay files");
        }
        int currentPage = 1;
        for (int i = 0; i < overlayFiles.length; i++) {
            File overlayFile = overlayFiles[i];
            int repeatCount = counts[i];

            // Load the overlay document to check its page count
            try (PDDocument overlayPdf = Loader.loadPDF(overlayFile)) {
                int overlayPageCount = overlayPdf.getNumberOfPages();
                for (int j = 0; j < repeatCount; j++) {
                    for (int page = 0; page < overlayPageCount; page++) {
                        if (currentPage > basePageCount) break;
                        overlayGuide.put(currentPage++, overlayFile.getAbsolutePath());
                    }
                }
            }
        }
    }
