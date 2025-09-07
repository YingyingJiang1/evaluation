    private static void declaredOneField(Field field, Map<Integer, List<FieldWrapper>> orderFieldMap,
        Map<Integer, FieldWrapper> indexFieldMap, Set<String> ignoreSet,
        ExcelIgnoreUnannotated excelIgnoreUnannotated) {
        String fieldName = FieldUtils.resolveCglibFieldName(field);
        FieldWrapper fieldWrapper = new FieldWrapper();
        fieldWrapper.setField(field);
        fieldWrapper.setFieldName(fieldName);

        ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);

        if (excelIgnore != null) {
            ignoreSet.add(fieldName);
            return;
        }
        ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
        boolean noExcelProperty = excelProperty == null && excelIgnoreUnannotated != null;
        if (noExcelProperty) {
            ignoreSet.add(fieldName);
            return;
        }
        boolean isStaticFinalOrTransient =
            (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
                || Modifier.isTransient(field.getModifiers());
        if (excelProperty == null && isStaticFinalOrTransient) {
            ignoreSet.add(fieldName);
            return;
        }
        // set heads
        if (excelProperty != null) {
            fieldWrapper.setHeads(excelProperty.value());
        }

        if (excelProperty != null && excelProperty.index() >= 0) {
            if (indexFieldMap.containsKey(excelProperty.index())) {
                throw new ExcelCommonException(
                    "The index of '" + indexFieldMap.get(excelProperty.index()).getFieldName()
                        + "' and '" + field.getName() + "' must be inconsistent");
            }
            indexFieldMap.put(excelProperty.index(), fieldWrapper);
            return;
        }

        int order = Integer.MAX_VALUE;
        if (excelProperty != null) {
            order = excelProperty.order();
        }
        List<FieldWrapper> orderFieldList = orderFieldMap.computeIfAbsent(order, key -> ListUtils.newArrayList());
        orderFieldList.add(fieldWrapper);
    }
