        private boolean isMatch(AuditEvent event) {
            return isInScopeOfSuppression(event)
                    && isCheckMatch(event)
                    && isIdMatch(event)
                    && isMessageMatch(event);
        }
