                    @Override
                    public void handle(Future<Void> v) {
                        if (count.decrementAndGet() == 0 && completionHandler != null) {
                            completionHandler.handle(null);
                        }
                    }
