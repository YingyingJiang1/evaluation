        @Override
        public Thread newThread(Runnable r) {
            final Thread t = new Thread(r, "arthas-UserStat");
            t.setDaemon(true);
            return t;
        }
