    private int getJobId(String arg) {
        int result = -1;
        try {
            if (arg.startsWith("%")) {
                result = Integer.parseInt(arg.substring(1));
            } else {
                result = Integer.parseInt(arg);
            }
        } catch (Exception e) {
        }
        return result;
    }
