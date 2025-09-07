    public boolean containsTextInFile(PDDocument pdfDocument, String text, String pagesToCheck)
            throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        String pdfText = "";

        if (pagesToCheck == null || "all".equals(pagesToCheck)) {
            pdfText = textStripper.getText(pdfDocument);
        } else {
            // remove whitespaces
            pagesToCheck = pagesToCheck.replaceAll("\\s+", "");

            String[] splitPoints = pagesToCheck.split(",");
            for (String splitPoint : splitPoints) {
                if (splitPoint.contains("-")) {
                    // Handle page ranges
                    String[] range = splitPoint.split("-");
                    int startPage = Integer.parseInt(range[0]);
                    int endPage = Integer.parseInt(range[1]);

                    for (int i = startPage; i <= endPage; i++) {
                        textStripper.setStartPage(i);
                        textStripper.setEndPage(i);
                        pdfText += textStripper.getText(pdfDocument);
                    }
                } else {
                    // Handle individual page
                    int page = Integer.parseInt(splitPoint);
                    textStripper.setStartPage(page);
                    textStripper.setEndPage(page);
                    pdfText += textStripper.getText(pdfDocument);
                }
            }
        }

        pdfDocument.close();

        return pdfText.contains(text);
    }
