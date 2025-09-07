    protected void addImagesMetadataItem(final LayoutInflater inflater,
                                         final LinearLayout layout,
                                         @StringRes final int type,
                                         final List<Image> images) {
        final String preferredImageUrl = ImageStrategy.choosePreferredImage(images);
        if (preferredImageUrl == null) {
            return; // null will be returned in case there is no image
        }

        final ItemMetadataBinding itemBinding =
                ItemMetadataBinding.inflate(inflater, layout, false);
        itemBinding.metadataTypeView.setText(type);

        final SpannableStringBuilder urls = new SpannableStringBuilder();
        for (final Image image : images) {
            if (urls.length() != 0) {
                urls.append(", ");
            }
            final int entryBegin = urls.length();

            if (image.getHeight() != Image.HEIGHT_UNKNOWN
                    || image.getWidth() != Image.WIDTH_UNKNOWN
                    // if even the resolution level is unknown, ?x? will be shown
                    || image.getEstimatedResolutionLevel() == Image.ResolutionLevel.UNKNOWN) {
                urls.append(imageSizeToText(image.getHeight()));
                urls.append('x');
                urls.append(imageSizeToText(image.getWidth()));
            } else {
                switch (image.getEstimatedResolutionLevel()) {
                    case LOW -> urls.append(getString(R.string.image_quality_low));
                    case MEDIUM -> urls.append(getString(R.string.image_quality_medium));
                    case HIGH -> urls.append(getString(R.string.image_quality_high));
                    default -> {
                        // unreachable, Image.ResolutionLevel.UNKNOWN is already filtered out
                    }
                }
            }

            urls.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull final View widget) {
                    ShareUtils.openUrlInBrowser(requireContext(), image.getUrl());
                }
            }, entryBegin, urls.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (preferredImageUrl.equals(image.getUrl())) {
                urls.setSpan(new StyleSpan(Typeface.BOLD), entryBegin, urls.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        itemBinding.metadataContentView.setText(urls);
        itemBinding.metadataContentView.setMovementMethod(LinkMovementMethod.getInstance());
        layout.addView(itemBinding.getRoot());
    }
