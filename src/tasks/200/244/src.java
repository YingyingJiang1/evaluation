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
