    private void addClassLoading(JvmModel jvmModel) {
        String group = "CLASS-LOADING";
        jvmModel.addItem(group, "LOADED-CLASS-COUNT", classLoadingMXBean.getLoadedClassCount());
        jvmModel.addItem(group, "TOTAL-LOADED-CLASS-COUNT", classLoadingMXBean.getTotalLoadedClassCount());
        jvmModel.addItem(group, "UNLOADED-CLASS-COUNT", classLoadingMXBean.getUnloadedClassCount());
        jvmModel.addItem(group, "IS-VERBOSE", classLoadingMXBean.isVerbose());
    }
