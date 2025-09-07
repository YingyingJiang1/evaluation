    private void drawAttributeInfo(MBeanAttributeInfo[] attributes, TableElement table) {
        for (MBeanAttributeInfo attribute : attributes) {
            table.row(new LabelElement("MBeanAttributeInfo").style(Decoration.bold.fg(Color.red)));
            table.row(new LabelElement("Attribute:").style(Decoration.bold.fg(Color.yellow)));
            table.row("Name", attribute.getName());
            table.row("Description", attribute.getDescription());
            table.row("Readable", String.valueOf(attribute.isReadable()));
            table.row("Writable", String.valueOf(attribute.isWritable()));
            table.row("Is", String.valueOf(attribute.isIs()));
            table.row("Type", attribute.getType());
            drawDescriptorInfo("Attribute Descriptor:", attribute, table);
        }
    }
