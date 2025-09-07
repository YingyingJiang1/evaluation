    private BufferedImage softenEdges(
            BufferedImage image,
            int featherRadius,
            Color startColor,
            Color endColor,
            boolean vertical) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage output = new BufferedImage(width, height, image.getType());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int dx = Math.min(x, width - 1 - x);
                int dy = Math.min(y, height - 1 - y);
                int d = Math.min(dx, dy);
                float frac = vertical ? (float) y / (height - 1) : (float) x / (width - 1);
                int rBg =
                        Math.round(
                                startColor.getRed()
                                        + (endColor.getRed() - startColor.getRed()) * frac);
                int gBg =
                        Math.round(
                                startColor.getGreen()
                                        + (endColor.getGreen() - startColor.getGreen()) * frac);
                int bBg =
                        Math.round(
                                startColor.getBlue()
                                        + (endColor.getBlue() - startColor.getBlue()) * frac);
                int bgVal = new Color(rBg, gBg, bBg).getRGB();
                int fgVal = image.getRGB(x, y);
                float alpha = d < featherRadius ? (float) d / featherRadius : 1.0f;
                int blended = blendColors(fgVal, bgVal, alpha);
                output.setRGB(x, y, blended);
            }
        }
        return output;
    }
