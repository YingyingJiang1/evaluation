    private Object convertAttrValue(String attributeName, Object originAttrValue) {
        Object attrValue = originAttrValue;

        try {
            if (originAttrValue instanceof ObjectName) {
                attrValue = String.valueOf(originAttrValue);
            } else if (attrValue instanceof CompositeData) {
                //mbean java.lang:type=MemoryPool,name=*
                CompositeData compositeData = (CompositeData) attrValue;
                attrValue = convertCompositeData(attributeName, compositeData);
            } else if (attrValue instanceof CompositeData[]) {
                //mbean com.sun.management:type=HotSpotDiagnostic
                CompositeData[] compositeDataArray = (CompositeData[]) attrValue;
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(compositeDataArray.length);
                for (CompositeData compositeData : compositeDataArray) {
                    dataList.add(convertCompositeData(attributeName, compositeData));
                }
                attrValue = dataList;
            } else if (attrValue instanceof TabularData) {
                //mbean java.lang:type=GarbageCollector,name=*
                TabularData tabularData = (TabularData) attrValue;
                Collection<CompositeData> compositeDataList = (Collection<CompositeData>) tabularData.values();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(compositeDataList.size());
                for (CompositeData compositeData : compositeDataList) {
                    dataList.add(convertCompositeData(attributeName, compositeData));
                }
                attrValue = dataList;
            }
        } catch (Throwable e) {
            logger.error("convert mbean attribute error, attribute: {}={}", attributeName, originAttrValue, e);
            attrValue = String.valueOf(originAttrValue);
        }
        return attrValue;
    }
