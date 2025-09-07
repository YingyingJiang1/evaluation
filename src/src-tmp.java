// id:415
        void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            Observer<? super R> downstream = this.downstream;
            AtomicThrowable errors = this.errors;
            AtomicReference<SwitchMapMaybeObserver<R>> inner = this.inner;

            for (;;) {

                for (;;) {
                    if (cancelled) {
                        return;
                    }

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

                    if (empty || current.item == null) {
                        break;
                    }

                    inner.compareAndSet(current, null);

                    downstream.onNext(current.item);
                }

                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

// id:416
        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
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

// id:417
        void drainLoop() {
            Observer<? super T> actual = this.downstream;
            int missed = 1;
            for (;;) {

                for (;;) {
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

// id:418
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

// id:419
        void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            EqualObserver<T>[] as = observers;

            final EqualObserver<T> observer1 = as[0];
            final SpscLinkedArrayQueue<T> q1 = observer1.queue;
            final EqualObserver<T> observer2 = as[1];
            final SpscLinkedArrayQueue<T> q2 = observer2.queue;

            for (;;) {

                for (;;) {
                    if (cancelled) {
                        q1.clear();
                        q2.clear();
                        return;
                    }

                    boolean d1 = observer1.done;

                    if (d1) {
                        Throwable e = observer1.error;
                        if (e != null) {
                            cancel(q1, q2);

                            downstream.onError(e);
                            return;
                        }
                    }

                    boolean d2 = observer2.done;
                    if (d2) {
                        Throwable e = observer2.error;
                        if (e != null) {
                            cancel(q1, q2);

                            downstream.onError(e);
                            return;
                        }
                    }

                    if (v1 == null) {
                        v1 = q1.poll();
                    }
                    boolean e1 = v1 == null;

                    if (v2 == null) {
                        v2 = q2.poll();
                    }
                    boolean e2 = v2 == null;

                    if (d1 && d2 && e1 && e2) {
                        downstream.onSuccess(true);
                        return;
                    }
                    if ((d1 && d2) && (e1 != e2)) {
                        cancel(q1, q2);

                        downstream.onSuccess(false);
                        return;
                    }

                    if (!e1 && !e2) {
                        boolean c;

                        try {
                            c = comparer.test(v1, v2);
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            cancel(q1, q2);

                            downstream.onError(ex);
                            return;
                        }

                        if (!c) {
                            cancel(q1, q2);

                            downstream.onSuccess(false);
                            return;
                        }

                        v1 = null;
                        v2 = null;
                    }

                    if (e1 || e2) {
                        break;
                    }
                }

                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

// id:420
        void drainLoop() {
            int missed = 1;

            JoinInnerSubscriber<T>[] s = this.subscribers;
            int n = s.length;
            Subscriber<? super T> a = this.downstream;

            for (;;) {

                long r = requested.get();
                long e = 0;

                middle:
                while (e != r) {
                    if (cancelled) {
                        cleanup();
                        return;
                    }

                    Throwable ex = errors.get();
                    if (ex != null) {
                        cleanup();
                        a.onError(ex);
                        return;
                    }

                    boolean d = done.get() == 0;

                    boolean empty = true;

                    for (int i = 0; i < s.length; i++) {
                        JoinInnerSubscriber<T> inner = s[i];
                        SimplePlainQueue<T> q = inner.queue;
                        if (q != null) {
                            T v = q.poll();

                            if (v != null) {
                                empty = false;
                                a.onNext(v);
                                inner.requestOne();
                                if (++e == r) {
                                    break middle;
                                }
                            }
                        }
                    }

                    if (d && empty) {
                        a.onComplete();
                        return;
                    }

                    if (empty) {
                        break;
                    }
                }

                if (e == r) {
                    if (cancelled) {
                        cleanup();
                        return;
                    }

                    Throwable ex = errors.get();
                    if (ex != null) {
                        cleanup();
                        a.onError(ex);
                        return;
                    }

                    boolean d = done.get() == 0;

                    boolean empty = true;

                    for (int i = 0; i < n; i++) {
                        JoinInnerSubscriber<T> inner = s[i];

                        SimpleQueue<T> q = inner.queue;
                        if (q != null && !q.isEmpty()) {
                            empty = false;
                            break;
                        }
                    }

                    if (d && empty) {
                        a.onComplete();
                        return;
                    }
                }

                if (e != 0) {
                    BackpressureHelper.produced(requested, e);
                }

                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

// id:421
        @Override
        public void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            InnerQueuedSubscriber<R> inner = current;
            Subscriber<? super R> a = downstream;
            ErrorMode em = errorMode;

            for (;;) {
                long r = requested.get();
                long e = 0L;

                if (inner == null) {

                    if (em != ErrorMode.END) {
                        Throwable ex = errors.get();
                        if (ex != null) {
                            cancelAll();

                            errors.tryTerminateConsumer(downstream);
                            return;
                        }
                    }

                    boolean outerDone = done;

                    inner = subscribers.poll();

                    if (outerDone && inner == null) {
                        errors.tryTerminateConsumer(downstream);
                        return;
                    }

                    if (inner != null) {
                        current = inner;
                    }
                }

                boolean continueNextSource = false;

                if (inner != null) {
                    SimpleQueue<R> q = inner.queue();
                    if (q != null) {
                        while (e != r) {
                            if (cancelled) {
                                cancelAll();
                                return;
                            }

                            if (em == ErrorMode.IMMEDIATE) {
                                Throwable ex = errors.get();
                                if (ex != null) {
                                    current = null;
                                    inner.cancel();
                                    cancelAll();

                                    errors.tryTerminateConsumer(downstream);
                                    return;
                                }
                            }

                            boolean d = inner.isDone();

                            R v;

                            try {
                                v = q.poll();
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                current = null;
                                inner.cancel();
                                cancelAll();
                                a.onError(ex);
                                return;
                            }

                            boolean empty = v == null;

                            if (d && empty) {
                                inner = null;
                                current = null;
                                upstream.request(1);
                                continueNextSource = true;
                                break;
                            }

                            if (empty) {
                                break;
                            }

                            a.onNext(v);

                            e++;

                            inner.request(1L);
                        }

                        if (e == r) {
                            if (cancelled) {
                                cancelAll();
                                return;
                            }

                            if (em == ErrorMode.IMMEDIATE) {
                                Throwable ex = errors.get();
                                if (ex != null) {
                                    current = null;
                                    inner.cancel();
                                    cancelAll();

                                    errors.tryTerminateConsumer(downstream);
                                    return;
                                }
                            }

                            boolean d = inner.isDone();

                            boolean empty = q.isEmpty();

                            if (d && empty) {
                                inner = null;
                                current = null;
                                upstream.request(1);
                                continueNextSource = true;
                            }
                        }
                    }
                }

                if (e != 0L && r != Long.MAX_VALUE) {
                    requested.addAndGet(-e);
                }

                if (continueNextSource) {
                    continue;
                }

                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

// id:422
    @SuppressWarnings("unchecked")
    void remove(CacheDisposable<T> consumer) {
        for (;;) {
            CacheDisposable<T>[] current = observers.get();
            int n = current.length;
            if (n == 0) {
                return;
            }

            int j = -1;
            for (int i = 0; i < n; i++) {
                if (current[i] == consumer) {
                    j = i;
                    break;
                }
            }

            if (j < 0) {
                return;
            }
            CacheDisposable<T>[] next;

            if (n == 1) {
                next = EMPTY;
            } else {
                next = new CacheDisposable[n - 1];
                System.arraycopy(current, 0, next, 0, j);
                System.arraycopy(current, j + 1, next, j, n - j - 1);
            }

            if (observers.compareAndSet(current, next)) {
                return;
            }
        }
    }

// id:423
        void drainFused() {
            int missed = 1;

            for (;;) {
                if (disposed) {
                    return;
                }

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
                if (missed == 0) {
                    break;
                }
            }
        }

// id:424
    @SuppressWarnings("unchecked")
    void remove(PublishSubscription<T> ps) {
        for (;;) {
            PublishSubscription<T>[] a = subscribers.get();
            if (a == TERMINATED || a == EMPTY) {
                return;
            }

            int n = a.length;
            int j = -1;
            for (int i = 0; i < n; i++) {
                if (a[i] == ps) {
                    j = i;
                    break;
                }
            }

            if (j < 0) {
                return;
            }

            PublishSubscription<T>[] b;

            if (n == 1) {
                b = EMPTY;
            } else {
                b = new PublishSubscription[n - 1];
                System.arraycopy(a, 0, b, 0, j);
                System.arraycopy(a, j + 1, b, j, n - j - 1);
            }
            if (subscribers.compareAndSet(a, b)) {
                return;
            }
        }
    }

