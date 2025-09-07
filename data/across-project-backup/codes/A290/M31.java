    public static void merge(WriteFont source, WriteFont target) {
        if (source == null || target == null) {
            return;
        }
        if (StringUtils.isNotBlank(source.getFontName())) {
            target.setFontName(source.getFontName());
        }
        if (source.getFontHeightInPoints() != null) {
            target.setFontHeightInPoints(source.getFontHeightInPoints());
        }
        if (source.getItalic() != null) {
            target.setItalic(source.getItalic());
        }
        if (source.getStrikeout() != null) {
            target.setStrikeout(source.getStrikeout());
        }
        if (source.getColor() != null) {
            target.setColor(source.getColor());
        }
        if (source.getTypeOffset() != null) {
            target.setTypeOffset(source.getTypeOffset());
        }
        if (source.getUnderline() != null) {
            target.setUnderline(source.getUnderline());
        }
        if (source.getCharset() != null) {
            target.setCharset(source.getCharset());
        }
        if (source.getBold() != null) {
            target.setBold(source.getBold());
        }
    }
