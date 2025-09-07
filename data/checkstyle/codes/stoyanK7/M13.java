        @SuppressWarnings("unchecked")
        @Override
        public Object convert(Class type, Object value) {
            final StringTokenizer tokenizer = new StringTokenizer(
                value.toString().trim(), COMMA_SEPARATOR);
            final List<String> result = new ArrayList<>();

            while (tokenizer.hasMoreTokens()) {
                final String token = tokenizer.nextToken();
                result.add(token.trim());
            }

            return result.toArray(CommonUtil.EMPTY_STRING_ARRAY);
        }
