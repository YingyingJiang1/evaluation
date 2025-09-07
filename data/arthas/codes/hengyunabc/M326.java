    public static void trace(Throwable t) {
        if (canLog(Level.FINEST)) {
            t.printStackTrace(System.out);
        }
    }
