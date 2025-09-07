    public static Field getField(Class<?> clazz, String name) {
        for (Field field : getFields(clazz)) {
            if (ArthasCheckUtils.isEquals(field.getName(), name)) {
                return field;
            }
        }//for
        return null;
    }
