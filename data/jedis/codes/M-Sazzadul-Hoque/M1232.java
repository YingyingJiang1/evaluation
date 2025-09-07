        static Resource from(File file) {

            Objects.requireNonNull(file, "File must not be null");

            return () -> new FileInputStream(file);
        }
