        @Override
        void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            final SimplePlainQueue<Object> queue = this.queue;
            final Subscriber<? super Flowable<T>> downstream = this.downstream;
            UnicastProcessor<T> window = this.window;

            for (;;) {

                if (upstreamCancelled) {
                    queue.clear();
                    window = null;
                    this.window = null;
                } else {

                    boolean isDone = done;
                    Object o = queue.poll();
                    boolean isEmpty = o == null;

                    if (isDone && isEmpty) {
                        Throwable ex = error;
                        if (ex != null) {
                            if (window != null) {
                                window.onError(ex);
                            }
                            downstream.onError(ex);
                        } else {
                            if (window != null) {
                                window.onComplete();
                            }
                            downstream.onComplete();
                        }
                        cleanupResources();
                        upstreamCancelled = true;
                        continue;
                    } else if (!isEmpty) {
                        if (o instanceof WindowBoundaryRunnable) {
                            WindowBoundaryRunnable boundary = (WindowBoundaryRunnable) o;
                            if (boundary.index == emitted || !restartTimerOnMaxSize) {
                                this.count = 0;
                                window = createNewWindow(window);
                            }
                        } else if (window != null) {
                            @SuppressWarnings("unchecked")
                            T item = (T)o;
                            window.onNext(item);

                            long count = this.count + 1;
                            if (count == maxSize) {
                                this.count = 0;
                                window = createNewWindow(window);
                            } else {
                                this.count = count;
                            }
                        }

                        continue;
                    }
                }

                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }
