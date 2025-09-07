    private Set<ObjectName> queryObjectNames() {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = new HashSet<ObjectName>();
        try {
            if (StringUtils.isEmpty(name)) {
                name = "*:*";
            }
            objectNames = platformMBeanServer.queryNames(new ObjectName(name), null);
        } catch (MalformedObjectNameException e) {
            logger.warn("queryObjectNames error", e);
        }
        return objectNames;
    }
