
    void drainLoop() {
        Observer<? super T> actual = this.downstream;
        int missed = 1;

        while (true) {
            while (true) {
                if (disposed) {
                    singleItem = null;
                    queue = null;
                    return;
                }

                if (errors.get() != null) {
                    singleItem = null;
                    queue = null;
                    errors.tryTerminateConsumer(actual);
                    return;
                }

                int os = otherState;
                if (os == OTHER_STATE_HAS_VALUE) {
                    T v = singleItem;
                    singleItem = null;
                    otherState = OTHER_STATE_CONSUMED_OR_EMPTY;
                    os = OTHER_STATE_CONSUMED_OR_EMPTY;
                    actual.onNext(v);
                }

                boolean d = mainDone;
                SimplePlainQueue<T> q = queue;
                T v = q != null ? q.poll() : null;
                boolean empty = v == null;

                if (d && empty && os == OTHER_STATE_CONSUMED_OR_EMPTY) {
                    queue = null;
                    actual.onComplete();
                    return;
                }

                if (empty) {
                    break;
                }

                actual.onNext(v);
            }

            missed = addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }


    @Override
    void drain() {
        if (getAndIncrement() != 0) return;

        int missed = 1;
        final SimplePlainQueue<Object> queue = this.queue;
        final Subscriber<? super Flowable<T>> downstream = this.downstream;
        UnicastProcessor<T> window = this.window;

        while (true) {
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
                        if (window != null) window.onError(ex);
                        downstream.onError(ex);
                    } else {
                        if (window != null) window.onComplete();
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
                        T item = (T) o;
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
            if (missed == 0) break;
        }
    }


    @SuppressWarnings("unchecked")
    void remove(PublishSubscription<T> ps) {
        while (true) {
            PublishSubscription<T>[] a = subscribers.get();
            if (a == TERMINATED || a == EMPTY) return;

            int n = a.length;
            int j = -1;
            for (int i = 0; i < n; i++) {
                if (a[i] == ps) {
                    j = i;
                    break;
                }
            }

            if (j < 0) return;

            PublishSubscription<T>[] b;
            if (n == 1) {
                b = EMPTY;
            } else {
                b = new PublishSubscription[n - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, n - j - 1);
            }
            if (subscribers.compareAndSet(a, b)) return;
        }
    }


    @Override
    public void onNext(T t) {
        if (done) return;
        boolean b;
        try {
            b = predicate.test(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            upstream.cancel();
            upstream = SubscriptionHelper.CANCELLED;
            onError(e);
            return;
        }
        if (!b) {
            done = true;
            upstream.cancel();
            upstream = SubscriptionHelper.CANCELLED;
            downstream.onSuccess(false);
        }
    }


    void drain() {
        if (getAndIncrement() != 0) return;

        int missed = 1;
        Observer<? super R> downstream = this.downstream;
        AtomicThrowable errors = this.errors;
        AtomicReference<SwitchMapMaybeObserver<R>> inner = this.inner;

        while (true) {
            while (true) {
                if (cancelled) return;

                if (errors.get() != null) {
                    if (!delayErrors) {
                        errors.tryTerminateConsumer(downstream);
                        return;
                    }
                }

                boolean d = done;
                SwitchMapMaybeObserver<R> current = inner.get();
                boolean empty = current == null;

                if (d && empty) {
                    errors.tryTerminateConsumer(downstream);
                    return;
                }

                if (empty || current.item == null) break;

                inner.compareAndSet(current, null);
                downstream.onNext(current.item);
            }

            missed = addAndGet(-missed);
            if (missed == 0) break;
        }
    }


    @SuppressWarnings("unchecked")
    void remove(CacheDisposable<T> consumer) {
        while (true) {
            CacheDisposable<T>[] current = observers.get();
            int n = current.length;
            if (n == 0) return;

            int j = -1;
            for (int i = 0; i < n; i++) {
                if (current[i] == consumer) {
                    j = i;
                    break;
                }
            }

            if (j < 0) return;
            CacheDisposable<T>[] next;

            if (n == 1) {
                next = EMPTY;
            } else {
                next = new CacheDisposable[n - 1];
                System.arraycopy(current, 0, next, 0, j);
                System.arraycopy(current, j + 1, next, j, n - j - 1);
            }

            if (observers.compareAndSet(current, next)) return;
        }
    }


    void drainFused() {
        int missed = 1;

        while (true) {
            if (disposed) return;

            boolean d = done;
            Throwable ex = error;

            if (!delayError && d && ex != null) {
                disposed = true;
                downstream.onError(error);
                worker.dispose();
                return;
            }

            downstream.onNext(null);

            if (d) {
                disposed = true;
                ex = error;
                if (ex != null) {
                    downstream.onError(ex);
                } else {
                    downstream.onComplete();
                }
                worker.dispose();
                return;
            }

            missed = addAndGet(-missed);
            if (missed == 0) break;
        }
    }
