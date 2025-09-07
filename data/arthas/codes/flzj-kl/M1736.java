    private String normalizePath(String path) {
        if (path == null) {
            return null;
        }

        path = path.replaceAll("\\.\\./", "").replaceAll("\\./", "");


        path = path.startsWith("/") ? path : "/" + path;


        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;


        String finalPath = path;
        boolean hasAllowedExtension = ALLOWED_EXTENSIONS.stream()
                .anyMatch(finalPath::endsWith);

        if (!hasAllowedExtension) {
            return null;
        }

        return path;
    }
