        public String getName() {
            if (classLoader != null) {
                return classLoader.toString();
            }
            return "BootstrapClassLoader";
        }
