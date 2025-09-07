        @Override
        public boolean matching(T target) {
            for (Matcher<T> matcher : matchers) {
                if (!matcher.matching(target)) {
                    return false;
                }
            }
            return true;
        }
