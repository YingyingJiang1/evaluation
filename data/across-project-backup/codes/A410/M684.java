    private void monitorSubscription(final ChannelInfo info) {
        final Consumer<Throwable> onError = (final Throwable throwable) -> {
            animate(binding.channelSubscribeButton, false, 100);
            showSnackBarError(new ErrorInfo(throwable, UserAction.SUBSCRIPTION_GET,
                    "Get subscription status", currentInfo));
        };

        final Observable<List<SubscriptionEntity>> observable = subscriptionManager
                .subscriptionTable()
                .getSubscriptionFlowable(info.getServiceId(), info.getUrl())
                .toObservable();

        disposables.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscribeUpdateMonitor(info), onError));

        disposables.add(observable
                .map(List::isEmpty)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isEmpty -> updateSubscribeButton(!isEmpty), onError));

        disposables.add(observable
                .map(List::isEmpty)
                .distinctUntilChanged()
                .skip(1) // channel has just been opened
                .filter(x -> NotificationHelper.areNewStreamsNotificationsEnabled(requireContext()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isEmpty -> {
                    if (!isEmpty) {
                        showNotifySnackbar();
                    }
                }, onError));
    }
