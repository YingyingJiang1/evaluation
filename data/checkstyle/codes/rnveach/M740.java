    @Override
    public boolean accept(TreeWalkerAuditEvent event) {
        if (event.getTokenType() != 0) {
            final XpathQueryGenerator xpathQueryGenerator =
                    new XpathQueryGenerator(event, tabWidth);
            final List<String> xpathQueries = xpathQueryGenerator.generate();
            if (!xpathQueries.isEmpty()) {
                final String query = String.join(DELIMITER, xpathQueries);
                MESSAGE_QUERY_MAP.put(event.getViolation(), query);
            }
        }
        return true;
    }
