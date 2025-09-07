    public static String objectToString(Object obj) {
        if (null == obj) {
            return Constants.EMPTY_STRING;
        }
        try {
            return obj.toString();
        } catch (Throwable t) {
            logger.error("objectToString error, obj class: {}", obj.getClass(), t);
            return "ERROR DATA!!! Method toString() throw exception. obj class: " + obj.getClass()
                    + ", exception class: " + t.getClass()
                    + ", exception message: " + t.getMessage();
        }
    }
