    private Future<Void> safeStart(Runnable starter) {
        if (uniqueStarterTask.compareAndSet(null, new CompletableFuture<Void>())) {
            try {
                starter.run();
                uniqueStarterTask.get().complete(null);
            } catch (Exception e) {
                uniqueStarterTask.get().completeExceptionally(e);
            }
        }
        return uniqueStarterTask.get();
    }
