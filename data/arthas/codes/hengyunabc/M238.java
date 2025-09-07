    public void setPath(String path) {
        path = path.trim();
        if (!path.startsWith("/")) {
            logger.warn("tunnel server path should start with / ! path: {}, try to auto add / .", path);
            path = "/" + path;
        }
        this.path = path;
    }
