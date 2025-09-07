    public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately higher than [%s]",
//					propertySource.getName(), relativePropertySourceName));
//		}
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = assertPresentAndGetIndex(relativePropertySourceName);
        addAtIndex(index, propertySource);
    }
