        public long pop() {
            if (pos > 0) {
                pos--;
                return array[pos];
            } else {
                pos = cap;
                pos--;
                return array[pos];
            }
        }
