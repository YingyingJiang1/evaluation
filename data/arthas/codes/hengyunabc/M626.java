        public Object pop() {
            if (pos > 0) {
                pos--;
                Object object = array[pos];
                array[pos] = null;
                return object;
            } else {
                pos = cap;
                pos--;
                Object object = array[pos];
                array[pos] = null;
                return object;
            }
        }
