        @SuppressWarnings("unchecked")
        @Override
        public Object convert(Class type, Object value) {
            return SeverityLevel.getInstance(value.toString());
        }
