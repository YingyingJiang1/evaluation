                    @Override
                    public String getText(PDDocument doc) throws IOException {
                        this.lineInfos.clear();
                        this.lineBuilder = new StringBuilder();
                        this.lastY = -1;
                        this.maxFontSizeInLine = 0.0f;
                        this.lineCount = 0;
                        super.getText(doc);
                        processLine(); // Process the last line

                        // Merge lines with same font size
                        List<LineInfo> mergedLineInfos = new ArrayList<>();
                        for (int i = 0; i < lineInfos.size(); i++) {
                            String mergedText = lineInfos.get(i).text;
                            float fontSize = lineInfos.get(i).fontSize;
                            while (i + 1 < lineInfos.size()
                                    && lineInfos.get(i + 1).fontSize == fontSize) {
                                mergedText += " " + lineInfos.get(i + 1).text;
                                i++;
                            }
                            mergedLineInfos.add(new LineInfo(mergedText, fontSize));
                        }

                        // Sort lines by font size in descending order and get the first one
                        mergedLineInfos.sort(
                                Comparator.comparing((LineInfo li) -> li.fontSize).reversed());
                        String title =
                                mergedLineInfos.isEmpty() ? null : mergedLineInfos.get(0).text;

                        return title != null
                                ? title
                                : (useFirstTextAsFallback
                                        ? (mergedLineInfos.isEmpty()
                                                ? null
                                                : mergedLineInfos.get(mergedLineInfos.size() - 1)
                                                        .text)
                                        : null);
                    }
