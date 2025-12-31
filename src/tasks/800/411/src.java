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
