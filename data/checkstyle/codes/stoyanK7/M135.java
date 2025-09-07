        private boolean isIdMatch(AuditEvent event) {
            boolean match = true;
            if (eventIdRegexp != null) {
                if (event.getModuleId() == null) {
                    match = false;
                }
                else {
                    final Matcher idMatcher = eventIdRegexp.matcher(event.getModuleId());
                    match = idMatcher.find();
                }
            }
            return match;
        }
