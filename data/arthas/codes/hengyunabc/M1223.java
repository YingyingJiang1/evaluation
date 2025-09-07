    public void replace(String name, PropertySource<?> propertySource) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(String.format("Replacing [%s] PropertySource with [%s]",
//					name, propertySource.getName()));
//		}
        int index = assertPresentAndGetIndex(name);
        this.propertySourceList.set(index, propertySource);
    }
