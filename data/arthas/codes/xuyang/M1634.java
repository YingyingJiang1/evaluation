    public static JavaObject toJavaObject(Object obj, int depth) {
        if (depth >= MAX_DEPTH) {
            return null;
        }

        if (obj == null) {
            return JavaObject.newBuilder().setNullValue(NullValue.getDefaultInstance()).build();
        }

        JavaObject.Builder objectBuilder = JavaObject.newBuilder();
        Class<? extends Object> objClazz = obj.getClass();
        objectBuilder.setClassName(objClazz.getName());

        // 基础类型
        if (isBasicType(objClazz)) {
            return objectBuilder.setBasicValue(createBasicValue(obj)).build();
        } else if (obj instanceof Collection) { // 集合
            return objectBuilder.setCollection(createCollectionValue((Collection<?>) obj, depth)).build();
        } else if (obj instanceof Map) { // map
            return objectBuilder.setMap(createMapValue((Map<?, ?>) obj, depth)).build();
        } else if (objClazz.isArray()) {
            return objectBuilder.setArrayValue(toArrayValue(obj, depth)).build();
        }

        Field[] fields = objClazz.getDeclaredFields();
        List<JavaField> javaFields = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            JavaField.Builder fieldBuilder = JavaField.newBuilder();
            fieldBuilder.setName(field.getName());

            try {
                Object fieldValue = field.get(obj);
                Class<?> fieldType = field.getType();

                if (fieldValue == null) {
                    fieldBuilder.setNullValue(NullValue.newBuilder().setClassName(fieldType.getName()).build());
                } else if (fieldType.isArray()) {
                    ArrayValue arrayValue = toArrayValue(fieldValue, depth + 1);
                    if (arrayValue != null) {
                        fieldBuilder.setArrayValue(arrayValue);
                    } else {
                        fieldBuilder.setUnexpandedObject(
                                UnexpandedObject.newBuilder().setClassName(fieldType.getName()).build());
                    }
                } else if (fieldType.isPrimitive() || isBasicType(fieldType)) {
                    BasicValue basicValue = createBasicValue(fieldValue);
                    fieldBuilder.setBasicValue(basicValue);
                } else if (fieldValue instanceof Collection) { // 集合
                    fieldBuilder.setCollection(createCollectionValue((Collection<?>) fieldValue, depth));
                } else if (fieldValue instanceof Map) { // map
                    fieldBuilder.setMap(createMapValue((Map<?, ?>) fieldValue, depth));
                } else {
                    JavaObject nestedObject = toJavaObject(fieldValue, depth + 1);
                    if (nestedObject != null) {
                        fieldBuilder.setObjectValue(nestedObject);
                    } else {
                        fieldBuilder.setUnexpandedObject(
                                UnexpandedObject.newBuilder().setClassName(fieldType.getName()).build());
                    }
                }
            } catch (IllegalAccessException e) {
                // TODO ignore ?
            }
            javaFields.add(fieldBuilder.build());
        }

        objectBuilder.setFields(JavaFields.newBuilder().addAllFields(javaFields).build());
        return objectBuilder.build();
    }
