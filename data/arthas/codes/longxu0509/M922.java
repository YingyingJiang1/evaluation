    public long parseSize(String s) throws Exception {
        s = s.toLowerCase();
        if (s.endsWith("b")) {
            return Long.parseLong(s.substring(0, s.length() - 1).trim());
        } else if (s.endsWith("k")) {
            return 1024 * Long.parseLong(s.substring(0, s.length() - 1).trim());
        } else if (s.endsWith("m")) {
            return 1024 * 1024 * Long.parseLong(s.substring(0, s.length() - 1).trim());
        } else if (s.endsWith("g")) {
            return 1024 * 1024 * 1024 * Long.parseLong(s.substring(0, s.length() - 1).trim());
        } else {
            try {
                return Long.parseLong(s);
            } catch (Exception e) {
                throw new NumberFormatException("'" + s + "' is not a valid size. Should be numeric value followed by a unit, i.e. 20M. Valid units k, M, G");
            }
        }
    }
