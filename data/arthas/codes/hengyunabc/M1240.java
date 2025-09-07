    @Override
    public T[] convert(String source, Class<T[]> targetType) {
        String[] strings = StringUtils.tokenizeToStringArray(source, ",");

        @SuppressWarnings("unchecked")
        T[] values = (T[]) Array.newInstance(targetType.getComponentType(), strings.length);
        for (int i = 0; i < strings.length; ++i) {
            @SuppressWarnings("unchecked")
            T value = (T) conversionService.convert(strings[i], targetType.getComponentType());

            values[i] = value;
        }

        return values;
    }
