    public PropertySource<?> remove(String name) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(String.format("Removing [%s] PropertySource", name));
//		}
        int index = this.propertySourceList.indexOf(PropertySource.named(name));
        return index == -1 ? null : this.propertySourceList.remove(index);
    }
