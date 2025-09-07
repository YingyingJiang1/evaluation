        @SuppressWarnings("unchecked")
        @Override
        @Nullable
        public Object convert(Class type, Object value) {
            final String url = value.toString();
            URI result = null;

            if (!CommonUtil.isBlank(url)) {
                try {
                    result = CommonUtil.getUriByFilename(url);
                }
                catch (CheckstyleException exc) {
                    throw new IllegalArgumentException(exc);
                }
            }

            return result;
        }
