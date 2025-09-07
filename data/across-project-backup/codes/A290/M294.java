    private static Font createFont(Workbook workbook, Font originFont, WriteFont writeFont) {
        Font font = workbook.createFont();
        if (originFont == null) {
            return font;
        }
        if (originFont instanceof XSSFFont) {
            XSSFFont xssfFont = (XSSFFont)font;
            XSSFFont xssfOriginFont = ((XSSFFont)originFont);
            xssfFont.setFontName(xssfOriginFont.getFontName());
            xssfFont.setFontHeightInPoints(xssfOriginFont.getFontHeightInPoints());
            xssfFont.setItalic(xssfOriginFont.getItalic());
            xssfFont.setStrikeout(xssfOriginFont.getStrikeout());
            // Colors cannot be overwritten
            if (writeFont == null || writeFont.getColor() == null) {
                xssfFont.setColor(Optional.of(xssfOriginFont)
                    .map(XSSFFont::getXSSFColor)
                    .map(XSSFColor::getRGB)
                    .map(rgb -> new XSSFColor(rgb, null))
                    .orElse(null));
            }
            xssfFont.setTypeOffset(xssfOriginFont.getTypeOffset());
            xssfFont.setUnderline(xssfOriginFont.getUnderline());
            xssfFont.setCharSet(xssfOriginFont.getCharSet());
            xssfFont.setBold(xssfOriginFont.getBold());
            return xssfFont;
        } else if (originFont instanceof HSSFFont) {
            HSSFFont hssfFont = (HSSFFont)font;
            HSSFFont hssfOriginFont = (HSSFFont)originFont;
            hssfFont.setFontName(hssfOriginFont.getFontName());
            hssfFont.setFontHeightInPoints(hssfOriginFont.getFontHeightInPoints());
            hssfFont.setItalic(hssfOriginFont.getItalic());
            hssfFont.setStrikeout(hssfOriginFont.getStrikeout());
            hssfFont.setColor(hssfOriginFont.getColor());
            hssfFont.setTypeOffset(hssfOriginFont.getTypeOffset());
            hssfFont.setUnderline(hssfOriginFont.getUnderline());
            hssfFont.setCharSet(hssfOriginFont.getCharSet());
            hssfFont.setBold(hssfOriginFont.getBold());
            return hssfFont;
        }
        return font;
    }
