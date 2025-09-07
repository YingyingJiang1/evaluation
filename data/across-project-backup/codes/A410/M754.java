        private void runOnVisible(final Consumer<AppCompatActivity> runnable) {
            getActivityContext().ifPresentOrElse(context -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    context.runOnUiThread(() -> {
                        runnable.accept(context);
                        inFlight(false);
                    });
                } else {
                    getLifecycle().addObserver(new DefaultLifecycleObserver() {
                        @Override
                        public void onResume(@NonNull final LifecycleOwner owner) {
                            getLifecycle().removeObserver(this);
                            getActivityContext().ifPresentOrElse(context ->
                                    context.runOnUiThread(() -> {
                                        runnable.accept(context);
                                        inFlight(false);
                                    }),
                                    () -> inFlight(false)
                            );
                        }
                    });
                    // this trick doesn't seem to work on Android 10+ (API 29)
                    // which places restrictions on starting activities from the background
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
                            && !context.isChangingConfigurations()) {
                        // try to bring the activity back to front if minimised
                        final Intent i = new Intent(context, RouterActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                    }
                }

            }, () ->
                // this branch is executed if there is no activity context
                inFlight(false)
            );
        }
