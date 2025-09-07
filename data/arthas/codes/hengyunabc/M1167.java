    public static boolean isSubFile(File parent, File child) throws IOException {
        String parentPath = parent.getCanonicalPath();
        String childPath = child.getCanonicalPath();
        if (parentPath.equals(childPath) || childPath.startsWith(parent.getCanonicalPath() + File.separator)) {
            return true;
        }
        return false;
    }
