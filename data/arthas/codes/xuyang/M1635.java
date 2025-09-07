    private static ArrayValue toArrayValue(Object array, int depth) {
        if (array == null || depth >= MAX_DEPTH) {
            return null;
        }

        ArrayValue.Builder arrayBuilder = ArrayValue.newBuilder();
        Class<?> componentType = array.getClass().getComponentType();

        arrayBuilder.setClassName(componentType.getName());

        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(array, i);

            if (element != null) {
                if (componentType.isArray()) {
                    ArrayValue nestedArrayValue = toArrayValue(element, depth + 1);
                    if (nestedArrayValue != null) {
                        arrayBuilder.addElements(ArrayElement.newBuilder().setArrayValue(nestedArrayValue));
                    } else {
                        arrayBuilder.addElements(ArrayElement.newBuilder().setUnexpandedObject(
                                UnexpandedObject.newBuilder().setClassName(element.getClass().getName()).build()));
                    }

                } else if (componentType.isPrimitive() || isBasicType(componentType)) {
                    BasicValue basicValue = createBasicValue(element);
                    arrayBuilder.addElements(ArrayElement.newBuilder().setBasicValue(basicValue));
                } else {
                    JavaObject nestedObject = toJavaObject(element, depth + 1);
                    if (nestedObject != null) {
                        arrayBuilder.addElements(ArrayElement.newBuilder().setObjectValue(nestedObject));
                    } else {
                        arrayBuilder.addElements(ArrayElement.newBuilder().setUnexpandedObject(
                                UnexpandedObject.newBuilder().setClassName(element.getClass().getName()).build()));
                    }

                }
            } else {
                arrayBuilder.addElements(ArrayElement.newBuilder()
                        .setNullValue(NullValue.newBuilder().setClassName(componentType.getName()).build()));
            }
        }

        return arrayBuilder.build();
    }
