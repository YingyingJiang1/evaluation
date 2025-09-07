    @Override
    public String toString() {
        initMethod();
        if (constructor != null) {
            return constructor.toString();
        } else if (method != null) {
            return method.toString();
        }
        return "ERROR_METHOD";
    }
