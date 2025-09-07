    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof CustomJavaFileObject) {
            return ((CustomJavaFileObject) file).getClassName();
        } else {
            /**
             * if it's not CustomJavaFileObject, then it's coming from standard file manager
             * - let it handle the file
             */
            return super.inferBinaryName(location, file);
        }
    }
