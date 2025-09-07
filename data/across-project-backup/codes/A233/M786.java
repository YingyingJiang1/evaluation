    @Override
    protected void writeString(String text, List<TextPosition> textPositions) {
        for (MatchInfo match : findOccurrencesInText(searchText, text.toLowerCase())) {
            int index = match.startIndex;
            if (index + match.matchLength <= textPositions.size()) {
                // Initial values based on the first character
                TextPosition first = textPositions.get(index);
                float minX = first.getX();
                float minY = first.getY();
                float maxX = first.getX() + first.getWidth();
                float maxY = first.getY() + first.getHeight();

                // Loop over the rest of the characters and adjust bounding box values
                for (int i = index; i < index + match.matchLength; i++) {
                    TextPosition position = textPositions.get(i);
                    minX = Math.min(minX, position.getX());
                    minY = Math.min(minY, position.getY());
                    maxX = Math.max(maxX, position.getX() + position.getWidth());
                    maxY = Math.max(maxY, position.getY() + position.getHeight());
                }

                textOccurrences.add(
                        new PDFText(getCurrentPageNo() - 1, minX, minY, maxX, maxY, text));
            }
        }
    }
