            @Override
            public void onError(Throwable t) {
                logger.error("keep alive error: " + t.getMessage());
                t.printStackTrace();
            }
