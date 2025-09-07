    public static boolean isBlankImage(
            BufferedImage image, int threshold, double whitePercent, int blurSize) {
        if (image == null) {
            log.info("Error: Image is null");
            return false;
        }

        // Convert to binary image based on the threshold
        int whitePixels = 0;
        int totalPixels = image.getWidth() * image.getHeight();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int color = image.getRGB(j, i) & 0xFF;
                if (color >= 255 - threshold) {
                    whitePixels++;
                }
            }
        }

        double whitePixelPercentage = (whitePixels / (double) totalPixels) * 100;
        log.info(String.format("Page has white pixel percent of %.2f%%", whitePixelPercentage));

        return whitePixelPercentage >= whitePercent;
    }
