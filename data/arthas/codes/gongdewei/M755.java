            @Override
            public int compare(ThreadVO o1, ThreadVO o2) {
                long l1 = deltas.get(o1);
                long l2 = deltas.get(o2);
                if (l1 < l2) {
                    return 1;
                } else if (l1 > l2) {
                    return -1;
                } else {
                    return 0;
                }
            }
