    public static void showMetaInfoInTextView(@Nullable final List<MetaInfo> metaInfos,
                                              final TextView metaInfoTextView,
                                              final View metaInfoSeparator,
                                              final CompositeDisposable disposables) {
        final Context context = metaInfoTextView.getContext();
        if (metaInfos == null || metaInfos.isEmpty()
                || !PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.show_meta_info_key), true)) {
            metaInfoTextView.setVisibility(View.GONE);
            metaInfoSeparator.setVisibility(View.GONE);

        } else {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final MetaInfo metaInfo : metaInfos) {
                if (!isNullOrEmpty(metaInfo.getTitle())) {
                    stringBuilder.append("<b>").append(metaInfo.getTitle()).append("</b>")
                            .append(Localization.DOT_SEPARATOR);
                }

                String content = metaInfo.getContent().getContent().trim();
                if (content.endsWith(".")) {
                    content = content.substring(0, content.length() - 1); // remove . at end
                }
                stringBuilder.append(content);

                for (int i = 0; i < metaInfo.getUrls().size(); i++) {
                    if (i == 0) {
                        stringBuilder.append(Localization.DOT_SEPARATOR);
                    } else {
                        stringBuilder.append("<br/><br/>");
                    }

                    stringBuilder
                            .append("<a href=\"").append(metaInfo.getUrls().get(i)).append("\">")
                            .append(capitalizeIfAllUppercase(metaInfo.getUrlTexts().get(i).trim()))
                            .append("</a>");
                }
            }

            metaInfoSeparator.setVisibility(View.VISIBLE);
            TextLinkifier.fromHtml(metaInfoTextView, stringBuilder.toString(),
                    HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING, null, null, disposables,
                    SET_LINK_MOVEMENT_METHOD);
        }
    }
