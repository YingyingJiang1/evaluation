        private static void handleInternalLink(StringBuilder description,
                                               String moduleName, String value)
                throws MacroExecutionException {
            String href = value;
            href = href.replace(CHECKSTYLE_ORG_URL, "");
            // Remove first and last characters, they are always double quotes
            href = href.substring(1, href.length() - 1);

            final String relativeHref = getLinkToDocument(moduleName, href);
            final char doubleQuote = '\"';
            description.append(doubleQuote).append(relativeHref).append(doubleQuote);
        }
