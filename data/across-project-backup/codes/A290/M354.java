    private static Map<Integer, FieldWrapper> buildSortedAllFieldMap(Map<Integer, List<FieldWrapper>> orderFieldMap,
        Map<Integer, FieldWrapper> indexFieldMap) {

        Map<Integer, FieldWrapper> sortedAllFieldMap = new HashMap<>(
            (orderFieldMap.size() + indexFieldMap.size()) * 4 / 3 + 1);

        Map<Integer, FieldWrapper> tempIndexFieldMap = new HashMap<>(indexFieldMap);
        int index = 0;
        for (List<FieldWrapper> fieldList : orderFieldMap.values()) {
            for (FieldWrapper field : fieldList) {
                while (tempIndexFieldMap.containsKey(index)) {
                    sortedAllFieldMap.put(index, tempIndexFieldMap.get(index));
                    tempIndexFieldMap.remove(index);
                    index++;
                }
                sortedAllFieldMap.put(index, field);
                index++;
            }
        }
        sortedAllFieldMap.putAll(tempIndexFieldMap);
        return sortedAllFieldMap;
    }
