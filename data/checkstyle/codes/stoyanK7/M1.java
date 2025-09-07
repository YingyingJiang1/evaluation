    private static BeanUtilsBean createBeanUtilsBean() {
        final ConvertUtilsBean cub = new ConvertUtilsBean();

        registerIntegralTypes(cub);
        registerCustomTypes(cub);

        return new BeanUtilsBean(cub, new PropertyUtilsBean());
    }
