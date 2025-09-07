    private void addGarbageCollectors(JvmModel jvmModel) {
        String group = "GARBAGE-COLLECTORS";
        for (GarbageCollectorMXBean gcMXBean : garbageCollectorMXBeans) {
            Map<String, Object> gcInfo = new LinkedHashMap<String, Object>();
            gcInfo.put("name", gcMXBean.getName());
            gcInfo.put("collectionCount", gcMXBean.getCollectionCount());
            gcInfo.put("collectionTime", gcMXBean.getCollectionTime());

            jvmModel.addItem(group, gcMXBean.getName(), gcInfo, "count/time (ms)");
        }
    }
