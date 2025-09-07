    public static StyleProperty build(HeadStyle headStyle) {
        if (headStyle == null) {
            return null;
        }
        StyleProperty styleProperty = new StyleProperty();
        if (headStyle.dataFormat() >= 0) {
            DataFormatData dataFormatData = new DataFormatData();
            dataFormatData.setIndex(headStyle.dataFormat());
            styleProperty.setDataFormatData(dataFormatData);
        }
        styleProperty.setHidden(headStyle.hidden().getBooleanValue());
        styleProperty.setLocked(headStyle.locked().getBooleanValue());
        styleProperty.setQuotePrefix(headStyle.quotePrefix().getBooleanValue());
        styleProperty.setHorizontalAlignment(headStyle.horizontalAlignment().getPoiHorizontalAlignment());
        styleProperty.setWrapped(headStyle.wrapped().getBooleanValue());
        styleProperty.setVerticalAlignment(headStyle.verticalAlignment().getPoiVerticalAlignmentEnum());
        if (headStyle.rotation() >= 0) {
            styleProperty.setRotation(headStyle.rotation());
        }
        if (headStyle.indent() >= 0) {
            styleProperty.setIndent(headStyle.indent());
        }
        styleProperty.setBorderLeft(headStyle.borderLeft().getPoiBorderStyle());
        styleProperty.setBorderRight(headStyle.borderRight().getPoiBorderStyle());
        styleProperty.setBorderTop(headStyle.borderTop().getPoiBorderStyle());
        styleProperty.setBorderBottom(headStyle.borderBottom().getPoiBorderStyle());
        if (headStyle.leftBorderColor() >= 0) {
            styleProperty.setLeftBorderColor(headStyle.leftBorderColor());
        }
        if (headStyle.rightBorderColor() >= 0) {
            styleProperty.setRightBorderColor(headStyle.rightBorderColor());
        }
        if (headStyle.topBorderColor() >= 0) {
            styleProperty.setTopBorderColor(headStyle.topBorderColor());
        }
        if (headStyle.bottomBorderColor() >= 0) {
            styleProperty.setBottomBorderColor(headStyle.bottomBorderColor());
        }
        styleProperty.setFillPatternType(headStyle.fillPatternType().getPoiFillPatternType());
        if (headStyle.fillBackgroundColor() >= 0) {
            styleProperty.setFillBackgroundColor(headStyle.fillBackgroundColor());
        }
        if (headStyle.fillForegroundColor() >= 0) {
            styleProperty.setFillForegroundColor(headStyle.fillForegroundColor());
        }
        styleProperty.setShrinkToFit(headStyle.shrinkToFit().getBooleanValue());
        return styleProperty;
    }
