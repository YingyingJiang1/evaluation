        private static Pattern createPatternFromLine(String line) {
            final Pattern result;
            if (line.isEmpty()) {
                result = BLANK_LINE;
            }
            else {
                result = Pattern.compile(validateRegex(line));
            }
            return result;
        }
