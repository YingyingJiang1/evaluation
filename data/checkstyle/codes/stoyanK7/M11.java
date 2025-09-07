        @SuppressWarnings("unchecked")
        @Override
        public Object convert(Class type, Object value) {
            return Scope.getInstance(value.toString());
        }
