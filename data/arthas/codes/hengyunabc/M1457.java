    private static int findTheSpyAPIDepth(StackTraceElement[] stackTraceElementArray) {
        if (MAGIC_STACK_DEPTH > 0) {
            return MAGIC_STACK_DEPTH;
        }
        if (MAGIC_STACK_DEPTH > stackTraceElementArray.length) {
            return 0;
        }
        for (int i = 0; i < stackTraceElementArray.length; ++i) {
            if (SpyAPI.class.getName().equals(stackTraceElementArray[i].getClassName())) {
                MAGIC_STACK_DEPTH = i + 1;
                break;
            }
        }
        return MAGIC_STACK_DEPTH;
    }
