    public void setAccessible(boolean accessFlag) {
        initMethod();
        if (constructor != null) {
            constructor.setAccessible(accessFlag);
        } else if (method != null) {
            method.setAccessible(accessFlag);
        }
    }
