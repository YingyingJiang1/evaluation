    @Override
    public void terminate() {
        try {
            process.terminate();
        } catch (IllegalStateException ignore) {
            // Process already terminated, likely by itself
        } finally {
            controller.removeJob(this.id);
        }
    }
