    public void addLast(PropertySource<?> propertySource) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(String.format("Adding [%s] PropertySource with lowest search precedence",
//					propertySource.getName()));
//		}
        removeIfPresent(propertySource);
        this.propertySourceList.addLast(propertySource);
    }
