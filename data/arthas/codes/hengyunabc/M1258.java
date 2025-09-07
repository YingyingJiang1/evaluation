    @Override
    public boolean containsProperty(String name) {
        String[] propertyNames = getPropertyNames();
        if (propertyNames == null) {
            return false;
        }
        for (String temp : propertyNames) {
            if (temp.equals(name)) {

                return true;
            }
        }
        return false;

    }
