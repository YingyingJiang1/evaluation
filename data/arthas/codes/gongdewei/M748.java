    private MBeanAttributeVO createMBeanAttributeVO(String attributeName, Object originAttrValue) {
        Object attrValue = convertAttrValue(attributeName, originAttrValue);

        return new MBeanAttributeVO(attributeName, attrValue);
    }
