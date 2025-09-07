    public boolean isAccessible() {
        initMethod();
        if (this.method != null) {
            return method.isAccessible();
        } else if (this.constructor != null) {
            return constructor.isAccessible();
        }
        return false;
    }
