    public static void merge(WriteCellStyle source, WriteCellStyle target) {
        if (source == null || target == null) {
            return;
        }
        if (source.getDataFormatData() != null) {
            if (target.getDataFormatData() == null) {
                target.setDataFormatData(source.getDataFormatData());
            } else {
                DataFormatData.merge(source.getDataFormatData(), target.getDataFormatData());
            }
        }
        if (source.getWriteFont() != null) {
            if (target.getWriteFont() == null) {
                target.setWriteFont(source.getWriteFont());
            } else {
                WriteFont.merge(source.getWriteFont(), target.getWriteFont());
            }
        }
        if (source.getHidden() != null) {
            target.setHidden(source.getHidden());
        }
        if (source.getLocked() != null) {
            target.setLocked(source.getLocked());
        }
        if (source.getQuotePrefix() != null) {
            target.setQuotePrefix(source.getQuotePrefix());
        }
        if (source.getHorizontalAlignment() != null) {
            target.setHorizontalAlignment(source.getHorizontalAlignment());
        }
        if (source.getWrapped() != null) {
            target.setWrapped(source.getWrapped());
        }
        if (source.getVerticalAlignment() != null) {
            target.setVerticalAlignment(source.getVerticalAlignment());
        }
        if (source.getRotation() != null) {
            target.setRotation(source.getRotation());
        }
        if (source.getIndent() != null) {
            target.setIndent(source.getIndent());
        }
        if (source.getBorderLeft() != null) {
            target.setBorderLeft(source.getBorderLeft());
        }
        if (source.getBorderRight() != null) {
            target.setBorderRight(source.getBorderRight());
        }
        if (source.getBorderTop() != null) {
            target.setBorderTop(source.getBorderTop());
        }
        if (source.getBorderBottom() != null) {
            target.setBorderBottom(source.getBorderBottom());
        }
        if (source.getLeftBorderColor() != null) {
            target.setLeftBorderColor(source.getLeftBorderColor());
        }
        if (source.getRightBorderColor() != null) {
            target.setRightBorderColor(source.getRightBorderColor());
        }
        if (source.getTopBorderColor() != null) {
            target.setTopBorderColor(source.getTopBorderColor());
        }
        if (source.getBottomBorderColor() != null) {
            target.setBottomBorderColor(source.getBottomBorderColor());
        }
        if (source.getFillPatternType() != null) {
            target.setFillPatternType(source.getFillPatternType());
        }
        if (source.getFillBackgroundColor() != null) {
            target.setFillBackgroundColor(source.getFillBackgroundColor());
        }
        if (source.getFillForegroundColor() != null) {
            target.setFillForegroundColor(source.getFillForegroundColor());
        }
        if (source.getShrinkToFit() != null) {
            target.setShrinkToFit(source.getShrinkToFit());
        }
    }
