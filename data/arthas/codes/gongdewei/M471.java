    private static Element renderClassLoaderUrls(List<String> urls) {
        StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(url).append("\n");
        }
        return new LabelElement(sb.toString());
    }
