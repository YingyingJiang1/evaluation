        private boolean isExcludedClassRegexp(String candidateClassName) {
            boolean result = false;
            for (Pattern pattern : excludeClassesRegexps) {
                if (pattern.matcher(candidateClassName).matches()) {
                    result = true;
                    break;
                }
            }
            return result;
        }
