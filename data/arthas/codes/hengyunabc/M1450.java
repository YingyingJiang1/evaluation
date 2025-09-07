    public static List<String> toStringList(URL[] urls) {
        if (urls != null) {
            List<String> result = new ArrayList<String>(urls.length);
            for (URL url : urls) {
                result.add(url.toString());
            }
            return result;
        }
        return Collections.emptyList();
    }
