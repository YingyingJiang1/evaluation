    private boolean checkInterrupted(CommandProcess process) {
        if (!process.isRunning()) {
            return true;
        }
        if(isInterrupted){
            process.end(-1, "Processing has been interrupted");
            return true;
        } else {
            return false;
        }
    }
