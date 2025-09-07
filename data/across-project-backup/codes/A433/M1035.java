    private void notifyPostProcessing(int state) {
        String action;
        switch (state) {
            case 1:
                action = "Running";
                break;
            case 2:
                action = "Completed";
                break;
            default:
                action = "Failed";
        }

        Log.d(TAG, action + " postprocessing on " + storage.getName());

        if (state == 2) {
            psState = state;
            return;
        }

        synchronized (LOCK) {
            // don't return without fully write the current state
            psState = state;
            writeThisToFile();
        }
    }
