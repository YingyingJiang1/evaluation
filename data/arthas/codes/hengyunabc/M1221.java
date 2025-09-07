    public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately lower than [%s]",
//					propertySource.getName(), relativePropertySourceName));
//		}
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = assertPresentAndGetIndex(relativePropertySourceName);
        addAtIndex(index + 1, propertySource);
    }
