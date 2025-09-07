        @SuppressWarnings("unchecked")
        @Override
        public Object convert(Class type, Object value) {
            return CommonUtil.createPattern(value.toString());
        }
