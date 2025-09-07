    private float calculatePositionX(
            PDRectangle pageSize,
            int position,
            float contentWidth,
            PDFont font,
            float fontSize,
            String text,
            float margin)
            throws IOException {
        float actualWidth =
                (text != null) ? calculateTextWidth(text, font, fontSize) : contentWidth;
        switch (position % 3) {
            case 1: // Left
                return pageSize.getLowerLeftX() + margin;
            case 2: // Center
                return (pageSize.getWidth() - actualWidth) / 2;
            case 0: // Right
                return pageSize.getUpperRightX() - actualWidth - margin;
            default:
                return 0;
        }
    }
