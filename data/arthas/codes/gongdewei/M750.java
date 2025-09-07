    private Map<String, Object> convertCompositeData(String attributeName, CompositeData compositeData) {
        Set<String> keySet = compositeData.getCompositeType().keySet();
        String[] keys = keySet.toArray(new String[0]);
        Object[] values = compositeData.getAll(keys);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        for (int i = 0; i < keys.length; i++) {
            data.put(keys[i], convertAttrValue(attributeName + "." + keys[i], values[i]));
        }
        return data;
    }
