    @Nullable
    static String choosePreferredImage(@NonNull final List<Image> images,
                                       final PreferredImageQuality nonNoneQuality) {
        // this will be used to estimate the pixel count for images where only one of height or
        // width are known
        final double widthOverHeight = images.stream()
                .filter(image -> image.getHeight() != HEIGHT_UNKNOWN
                        && image.getWidth() != WIDTH_UNKNOWN)
                .mapToDouble(image -> ((double) image.getWidth()) / image.getHeight())
                .findFirst()
                .orElse(1.0);

        final Image.ResolutionLevel preferredLevel = nonNoneQuality.toResolutionLevel();
        final Comparator<Image> initialComparator = Comparator
                // the first step splits the images into groups of resolution levels
                .<Image>comparingInt(i -> {
                    if (i.getEstimatedResolutionLevel() == Image.ResolutionLevel.UNKNOWN) {
                        return 3; // avoid unknowns as much as possible
                    } else if (i.getEstimatedResolutionLevel() == preferredLevel) {
                        return 0; // prefer a matching resolution level
                    } else if (i.getEstimatedResolutionLevel() == Image.ResolutionLevel.MEDIUM) {
                        return 1; // the preferredLevel is only 1 "step" away (either HIGH or LOW)
                    } else {
                        return 2; // the preferredLevel is the furthest away possible (2 "steps")
                    }
                })
                // then each level's group is further split into two subgroups, one with known image
                // size (which is also the preferred subgroup) and the other without
                .thenComparing(image ->
                        image.getHeight() == HEIGHT_UNKNOWN && image.getWidth() == WIDTH_UNKNOWN);

        // The third step chooses, within each subgroup with known image size, the best image based
        // on how close its size is to BEST_LOW_H or BEST_MEDIUM_H (with proper units). Subgroups
        // without known image size will be left untouched since estimatePixelCount always returns
        // the same number for those.
        final Comparator<Image> finalComparator = switch (nonNoneQuality) {
            case NONE -> initialComparator; // unreachable
            case LOW -> initialComparator.thenComparingDouble(image -> {
                final double pixelCount = estimatePixelCount(image, widthOverHeight);
                return Math.abs(pixelCount - BEST_LOW_H * BEST_LOW_H * widthOverHeight);
            });
            case MEDIUM -> initialComparator.thenComparingDouble(image -> {
                final double pixelCount = estimatePixelCount(image, widthOverHeight);
                return Math.abs(pixelCount - BEST_MEDIUM_H * BEST_MEDIUM_H * widthOverHeight);
            });
            case HIGH -> initialComparator.thenComparingDouble(
                    // this is reversed with a - so that the highest resolution is chosen
                    i -> -estimatePixelCount(i, widthOverHeight));
        };

        return images.stream()
                // using "min" basically means "take the first group, then take the first subgroup,
                // then choose the best image, while ignoring all other groups and subgroups"
                .min(finalComparator)
                .map(Image::getUrl)
                .orElse(null);
    }
