    public static void fetchStreamInfoAndSaveToDatabase(@NonNull final Context context,
                                                        final int serviceId,
                                                        @NonNull final String url,
                                                        final Consumer<StreamInfo> callback) {
        Toast.makeText(context, R.string.loading_stream_details, Toast.LENGTH_SHORT).show();
        ExtractorHelper.getStreamInfo(serviceId, url, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    // save to database in the background (not on main thread)
                    Completable.fromAction(() -> NewPipeDatabase.getInstance(context)
                            .streamDAO().upsert(new StreamEntity(result)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .doOnError(throwable ->
                                    ErrorUtil.createNotification(context,
                                            new ErrorInfo(throwable, UserAction.REQUESTED_STREAM,
                                                    "Saving stream info to database", result)))
                            .subscribe();

                    // call callback on main thread with the obtained result
                    callback.accept(result);
                }, throwable -> ErrorUtil.createNotification(context,
                        new ErrorInfo(throwable, UserAction.REQUESTED_STREAM,
                                "Loading stream info: " + url, serviceId)
                ));
    }
