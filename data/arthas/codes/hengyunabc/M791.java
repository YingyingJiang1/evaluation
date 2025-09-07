        private boolean isLoaderMatch(RetransformEntry retransformEntry, ClassLoader loader) {
            if (loader == null) {
                return false;
            }
            if (retransformEntry.getClassLoaderClass() != null) {
                if (loader.getClass().getName().equals(retransformEntry.getClassLoaderClass())) {
                    return true;
                }
            }
            if (retransformEntry.getHashCode() != null) {
                String hashCode = Integer.toHexString(loader.hashCode());
                if (hashCode.equals(retransformEntry.getHashCode())) {
                    return true;
                }
            }
            return false;
        }
