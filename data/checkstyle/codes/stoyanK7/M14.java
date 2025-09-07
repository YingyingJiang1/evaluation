        @SuppressWarnings("unchecked")
        @Override
        public Object convert(Class type, Object value) {
            // Converts to a String and trims it for the tokenizer.
            final StringTokenizer tokenizer = new StringTokenizer(
                value.toString().trim(), COMMA_SEPARATOR);
            final List<AccessModifierOption> result = new ArrayList<>();

            while (tokenizer.hasMoreTokens()) {
                final String token = tokenizer.nextToken();
                result.add(AccessModifierOption.getInstance(token));
            }

            return result.toArray(EMPTY_MODIFIER_ARRAY);
        }
