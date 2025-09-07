        public void push(Object value) {
            if (pos < cap) {
                array[pos++] = value;
            } else {
                // if array is full, reset pos
                pos = 0;
                array[pos++] = value;
            }
        }
