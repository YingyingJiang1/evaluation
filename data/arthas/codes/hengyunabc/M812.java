        @Override
        public int compareTo(ClassLoaderInfo other) {
            if (other == null) {
                return -1;
            }
            if (other.classLoader == null) {
                return -1;
            }
            if (this.classLoader == null) {
                return -1;
            }

            return this.classLoader.getClass().getName().compareTo(other.classLoader.getClass().getName());
        }
