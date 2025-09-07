    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if(str != null && suffix != null) {
            if(str.endsWith(suffix)) {
                return true;
            } else if(str.length() < suffix.length()) {
                return false;
            } else {
                String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
                String lcSuffix = suffix.toLowerCase();
                return lcStr.equals(lcSuffix);
            }
        } else {
            return false;
        }
    }
