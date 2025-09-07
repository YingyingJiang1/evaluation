    private static Map<String, ExcelContentProperty> doDeclaredFieldContentMap(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<Field> tempFieldList = new ArrayList<>();
        Class<?> tempClass = clazz;
        while (tempClass != null) {
            Collections.addAll(tempFieldList, tempClass.getDeclaredFields());
            // Get the parent class and give it to yourself
            tempClass = tempClass.getSuperclass();
        }

        ContentStyle parentContentStyle = clazz.getAnnotation(ContentStyle.class);
        ContentFontStyle parentContentFontStyle = clazz.getAnnotation(ContentFontStyle.class);
        Map<String, ExcelContentProperty> fieldContentMap = MapUtils.newHashMapWithExpectedSize(
            tempFieldList.size());
        for (Field field : tempFieldList) {
            ExcelContentProperty excelContentProperty = new ExcelContentProperty();
            excelContentProperty.setField(field);

            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                Class<? extends Converter<?>> convertClazz = excelProperty.converter();
                if (convertClazz != AutoConverter.class) {
                    try {
                        Converter<?> converter = convertClazz.getDeclaredConstructor().newInstance();
                        excelContentProperty.setConverter(converter);
                    } catch (Exception e) {
                        throw new ExcelCommonException(
                            "Can not instance custom converter:" + convertClazz.getName());
                    }
                }
            }

            ContentStyle contentStyle = field.getAnnotation(ContentStyle.class);
            if (contentStyle == null) {
                contentStyle = parentContentStyle;
            }
            excelContentProperty.setContentStyleProperty(StyleProperty.build(contentStyle));

            ContentFontStyle contentFontStyle = field.getAnnotation(ContentFontStyle.class);
            if (contentFontStyle == null) {
                contentFontStyle = parentContentFontStyle;
            }
            excelContentProperty.setContentFontProperty(FontProperty.build(contentFontStyle));

            excelContentProperty.setDateTimeFormatProperty(
                DateTimeFormatProperty.build(field.getAnnotation(DateTimeFormat.class)));
            excelContentProperty.setNumberFormatProperty(
                NumberFormatProperty.build(field.getAnnotation(NumberFormat.class)));

            fieldContentMap.put(field.getName(), excelContentProperty);
        }
        return fieldContentMap;
    }
