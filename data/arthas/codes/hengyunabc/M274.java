        @SuppressWarnings("unchecked")
        V dereferenceValue(Object value) {
            if (value instanceof WeakKeyReference) {
                return ((Reference<V>) value).get();
            }

            return (V) value;
        }
