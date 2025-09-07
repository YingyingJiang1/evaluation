    public long parseTimespan(String s) throws Exception {
        s = s.toLowerCase();
        if (s.endsWith("s")) {
            return TimeUnit.NANOSECONDS.convert(Long.parseLong(s.substring(0, s.length() - 1).trim()), TimeUnit.SECONDS);
        } else if (s.endsWith("m")) {
            return 60 * TimeUnit.NANOSECONDS.convert(Long.parseLong(s.substring(0, s.length() - 1).trim()), TimeUnit.SECONDS);
        } else if (s.endsWith("h")) {
            return 60 * 60 * TimeUnit.NANOSECONDS.convert(Long.parseLong(s.substring(0, s.length() - 1).trim()), TimeUnit.SECONDS);
        } else if (s.endsWith("d")) {
            return 24 * 60 * 60 * TimeUnit.NANOSECONDS.convert(Long.parseLong(s.substring(0, s.length() - 1).trim()), TimeUnit.SECONDS);
        } else {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException var2) {
                throw new NumberFormatException("'" + s + "' is not a valid timespan. Shoule be numeric value followed by a unit, i.e. 20s. Valid units s, m, h and d.");
            }
        }
    }
