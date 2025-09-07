    public static Set<String> getProperties(Class<?> clss) {
        final Set<String> result = new TreeSet<>();
        final PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clss);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() != null) {
                result.add(propertyDescriptor.getName());
            }
        }

        return result;
    }
