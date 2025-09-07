    private static void resortField(WriteHolder writeHolder, FieldCache fieldCache) {
        if (!writeHolder.orderByIncludeColumn()) {
            return;
        }
        Map<Integer, FieldWrapper> indexFieldMap = fieldCache.getIndexFieldMap();

        Collection<String> includeColumnFieldNames = writeHolder.includeColumnFieldNames();
        if (!CollectionUtils.isEmpty(includeColumnFieldNames)) {
            // Field sorted map
            Map<String, Integer> filedIndexMap = MapUtils.newHashMap();
            int fieldIndex = 0;
            for (String includeColumnFieldName : includeColumnFieldNames) {
                filedIndexMap.put(includeColumnFieldName, fieldIndex++);
            }

            // rebuild sortedFieldMap
            Map<Integer, FieldWrapper> tempSortedFieldMap = MapUtils.newHashMap();
            fieldCache.getSortedFieldMap().forEach((index, field) -> {
                Integer tempFieldIndex = filedIndexMap.get(field.getFieldName());
                if (tempFieldIndex != null) {
                    tempSortedFieldMap.put(tempFieldIndex, field);

                    //  The user has redefined the ordering and the ordering of annotations needs to be invalidated
                    if (!tempFieldIndex.equals(index)) {
                        indexFieldMap.remove(index);
                    }
                }
            });
            fieldCache.setSortedFieldMap(tempSortedFieldMap);
            return;
        }

        Collection<Integer> includeColumnIndexes = writeHolder.includeColumnIndexes();
        if (!CollectionUtils.isEmpty(includeColumnIndexes)) {
            // Index sorted map
            Map<Integer, Integer> filedIndexMap = MapUtils.newHashMap();
            int fieldIndex = 0;
            for (Integer includeColumnIndex : includeColumnIndexes) {
                filedIndexMap.put(includeColumnIndex, fieldIndex++);
            }

            // rebuild sortedFieldMap
            Map<Integer, FieldWrapper> tempSortedFieldMap = MapUtils.newHashMap();
            fieldCache.getSortedFieldMap().forEach((index, field) -> {
                Integer tempFieldIndex = filedIndexMap.get(index);

                //  The user has redefined the ordering and the ordering of annotations needs to be invalidated
                if (tempFieldIndex != null) {
                    tempSortedFieldMap.put(tempFieldIndex, field);
                }
            });
            fieldCache.setSortedFieldMap(tempSortedFieldMap);
        }
    }
