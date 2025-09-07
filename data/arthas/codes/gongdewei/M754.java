                @Override
                public int compare(ThreadVO o1, ThreadVO o2) {
                    long l1 = o1.getTime();
                    long l2 = o2.getTime();
                    if (l1 < l2) {
                        return 1;
                    } else if (l1 > l2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
