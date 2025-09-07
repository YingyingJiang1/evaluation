                @Override
                public void run() {
                    if (grpcServer != null) {
                        grpcServer.shutdown();
                    }
                }
