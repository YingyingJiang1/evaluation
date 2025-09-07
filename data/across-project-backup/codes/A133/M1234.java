    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        if (sourceType == targetType) {
            return true;
        }

        if (targetType.isPrimitive()) {
            targetType = objectiveClass(targetType);
        }

        if (converters.containsKey(new ConvertiblePair(sourceType, targetType))) {
            return true;
        }
        if (targetType.isEnum()) {
            if (converters.containsKey(new ConvertiblePair(sourceType, Enum.class))) {
                return true;
            }
        }

        if (targetType.isArray()) {
            return true;
        }
        return false;
    }
