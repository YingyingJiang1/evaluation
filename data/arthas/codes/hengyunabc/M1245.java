    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String, Object> getSystemEnvironment() {
        try {
            return (Map) System.getenv();
        } catch (AccessControlException ex) {
            return (Map) new ReadOnlySystemAttributesMap() {
                @Override
                protected String getSystemAttribute(String attributeName) {
                    try {
                        return System.getenv(attributeName);
                    } catch (AccessControlException ex) {
                        return null;
                    }
                }
            };
        }
    }
