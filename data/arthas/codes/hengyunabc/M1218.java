    public void addFirst(PropertySource<?> propertySource) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(String.format("Adding [%s] PropertySource with highest search precedence",
//					propertySource.getName()));
//		}
        removeIfPresent(propertySource);
        this.propertySourceList.addFirst(propertySource);
    }
