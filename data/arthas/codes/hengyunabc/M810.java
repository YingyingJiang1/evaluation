        String hashCodeStr() {
            if (classLoader != null) {
                return "" + Integer.toHexString(classLoader.hashCode());
            }
            return "null";
        }
