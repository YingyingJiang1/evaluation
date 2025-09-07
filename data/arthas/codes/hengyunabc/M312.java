        @Override
        public V setValue(V value) {

            if (value == null) {
                throw new NullPointerException();
            }
            V v = super.setValue(value);
            put(getKey(), value);
            return v;
        }
