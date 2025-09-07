    public static FontProperty build(HeadFontStyle headFontStyle) {
        if (headFontStyle == null) {
            return null;
        }
        FontProperty styleProperty = new FontProperty();
        if (StringUtils.isNotBlank(headFontStyle.fontName())) {
            styleProperty.setFontName(headFontStyle.fontName());
        }
        if (headFontStyle.fontHeightInPoints() >= 0) {
            styleProperty.setFontHeightInPoints(headFontStyle.fontHeightInPoints());
        }
        styleProperty.setItalic(headFontStyle.italic().getBooleanValue());
        styleProperty.setStrikeout(headFontStyle.strikeout().getBooleanValue());
        if (headFontStyle.color() >= 0) {
            styleProperty.setColor(headFontStyle.color());
        }
        if (headFontStyle.typeOffset() >= 0) {
            styleProperty.setTypeOffset(headFontStyle.typeOffset());
        }
        if (headFontStyle.underline() >= 0) {
            styleProperty.setUnderline(headFontStyle.underline());
        }
        if (headFontStyle.charset() >= 0) {
            styleProperty.setCharset(headFontStyle.charset());
        }
        styleProperty.setBold(headFontStyle.bold().getBooleanValue());
        return styleProperty;
    }
