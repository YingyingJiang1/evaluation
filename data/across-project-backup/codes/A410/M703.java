    @Override
    protected Supplier<View> getListHeaderSupplier() {
        return () -> {
            final CommentRepliesHeaderBinding binding = CommentRepliesHeaderBinding
                    .inflate(activity.getLayoutInflater(), itemsList, false);
            final CommentsInfoItem item = commentsInfoItem;

            // load the author avatar
            PicassoHelper.loadAvatar(item.getUploaderAvatars()).into(binding.authorAvatar);
            binding.authorAvatar.setVisibility(ImageStrategy.shouldLoadImages()
                    ? View.VISIBLE : View.GONE);

            // setup author name and comment date
            binding.authorName.setText(item.getUploaderName());
            binding.uploadDate.setText(Localization.relativeTimeOrTextual(
                    getContext(), item.getUploadDate(), item.getTextualUploadDate()));
            binding.authorTouchArea.setOnClickListener(
                    v -> NavigationHelper.openCommentAuthorIfPresent(requireActivity(), item));

            // setup like count, hearted and pinned
            binding.thumbsUpCount.setText(
                    Localization.likeCount(requireContext(), item.getLikeCount()));
            // for heartImage goneMarginEnd was used, but there is no way to tell ConstraintLayout
            // not to use a different margin only when both the next two views are gone
            ((ConstraintLayout.LayoutParams) binding.thumbsUpCount.getLayoutParams())
                    .setMarginEnd(DeviceUtils.dpToPx(
                            (item.isHeartedByUploader() || item.isPinned() ? 8 : 16),
                            requireContext()));
            binding.heartImage.setVisibility(item.isHeartedByUploader() ? View.VISIBLE : View.GONE);
            binding.pinnedImage.setVisibility(item.isPinned() ? View.VISIBLE : View.GONE);

            // setup comment content
            TextLinkifier.fromDescription(binding.commentContent, item.getCommentText(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY, getServiceById(item.getServiceId()),
                    item.getUrl(), disposables, null);
            binding.commentContent.setMovementMethod(LongPressLinkMovementMethod.getInstance());
            return binding.getRoot();
        };
    }
