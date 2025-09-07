        private static String validateRegex(String input) {
            try {
                Pattern.compile(input);
                return input;
            }
            catch (final PatternSyntaxException exc) {
                throw new IllegalArgumentException("Invalid regex pattern: " + input, exc);
            }
        }
