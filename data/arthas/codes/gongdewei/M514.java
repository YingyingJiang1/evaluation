    private void drawDescriptorInfo(String title, DescriptorRead descriptorRead, TableElement table) {
        Descriptor descriptor = descriptorRead.getDescriptor();
        String[] fieldNames = descriptor.getFieldNames();
        if (fieldNames.length > 0) {
            table.row(new LabelElement(title).style(Decoration.bold.fg(Color.yellow)));
            for (String fieldName : fieldNames) {
                Object fieldValue = descriptor.getFieldValue(fieldName);
                table.row(fieldName, fieldValue == null ? "" : fieldValue.toString());
            }
        }
    }
