                @Override
                public Thread newThread(Runnable r) {
                    final Thread t = new Thread(r, "arthas-session-manager");
                    t.setDaemon(true);
                    return t;
                }
