    public static StyleProperty build(ContentStyle contentStyle) {
        if (contentStyle == null) {
            return null;
        }
        StyleProperty styleProperty = new StyleProperty();
        if (contentStyle.dataFormat() >= 0) {
            DataFormatData dataFormatData = new DataFormatData();
            dataFormatData.setIndex(contentStyle.dataFormat());
            styleProperty.setDataFormatData(dataFormatData);
        }
        styleProperty.setHidden(contentStyle.hidden().getBooleanValue());
        styleProperty.setLocked(contentStyle.locked().getBooleanValue());
        styleProperty.setQuotePrefix(contentStyle.quotePrefix().getBooleanValue());
        styleProperty.setHorizontalAlignment(contentStyle.horizontalAlignment().getPoiHorizontalAlignment());
        styleProperty.setWrapped(contentStyle.wrapped().getBooleanValue());
        styleProperty.setVerticalAlignment(contentStyle.verticalAlignment().getPoiVerticalAlignmentEnum());
        if (contentStyle.rotation() >= 0) {
            styleProperty.setRotation(contentStyle.rotation());
        }
        if (contentStyle.indent() >= 0) {
            styleProperty.setIndent(contentStyle.indent());
        }
        styleProperty.setBorderLeft(contentStyle.borderLeft().getPoiBorderStyle());
        styleProperty.setBorderRight(contentStyle.borderRight().getPoiBorderStyle());
        styleProperty.setBorderTop(contentStyle.borderTop().getPoiBorderStyle());
        styleProperty.setBorderBottom(contentStyle.borderBottom().getPoiBorderStyle());
        if (contentStyle.leftBorderColor() >= 0) {
            styleProperty.setLeftBorderColor(contentStyle.leftBorderColor());
        }
        if (contentStyle.rightBorderColor() >= 0) {
            styleProperty.setRightBorderColor(contentStyle.rightBorderColor());
        }
        if (contentStyle.topBorderColor() >= 0) {
            styleProperty.setTopBorderColor(contentStyle.topBorderColor());
        }
        if (contentStyle.bottomBorderColor() >= 0) {
            styleProperty.setBottomBorderColor(contentStyle.bottomBorderColor());
        }
        styleProperty.setFillPatternType(contentStyle.fillPatternType().getPoiFillPatternType());
        if (contentStyle.fillBackgroundColor() >= 0) {
            styleProperty.setFillBackgroundColor(contentStyle.fillBackgroundColor());
        }
        if (contentStyle.fillForegroundColor() >= 0) {
            styleProperty.setFillForegroundColor(contentStyle.fillForegroundColor());
        }
        styleProperty.setShrinkToFit(contentStyle.shrinkToFit().getBooleanValue());
        return styleProperty;
    }
