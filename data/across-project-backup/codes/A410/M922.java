    public static void fromDescription(@NonNull final TextView textView,
                                       @NonNull final Description description,
                                       final int htmlCompatFlag,
                                       @Nullable final StreamingService relatedInfoService,
                                       @Nullable final String relatedStreamUrl,
                                       @NonNull final CompositeDisposable disposables,
                                       @Nullable final Consumer<TextView> onCompletion) {
        switch (description.getType()) {
            case Description.HTML:
                TextLinkifier.fromHtml(textView, description.getContent(), htmlCompatFlag,
                        relatedInfoService, relatedStreamUrl, disposables, onCompletion);
                break;
            case Description.MARKDOWN:
                TextLinkifier.fromMarkdown(textView, description.getContent(),
                        relatedInfoService, relatedStreamUrl, disposables, onCompletion);
                break;
            case Description.PLAIN_TEXT: default:
                TextLinkifier.fromPlainText(textView, description.getContent(),
                        relatedInfoService, relatedStreamUrl, disposables, onCompletion);
                break;
        }
    }
