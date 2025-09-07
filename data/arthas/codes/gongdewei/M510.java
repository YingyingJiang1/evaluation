    private void drawMetaInfo(MBeanInfo mBeanInfo, String objectName, TableElement table) {
        table.row(new LabelElement("MBeanInfo").style(Decoration.bold.fg(Color.red)));
        table.row(new LabelElement("Info:").style(Decoration.bold.fg(Color.yellow)));
        table.row("ObjectName", objectName);
        table.row("ClassName", mBeanInfo.getClassName());
        table.row("Description", mBeanInfo.getDescription());
        drawDescriptorInfo("Info Descriptor:", mBeanInfo, table);
        MBeanConstructorInfo[] constructors = mBeanInfo.getConstructors();
        if (constructors.length > 0) {
            for (int i = 0; i < constructors.length; i++) {
                table.row(new LabelElement("Constructor-" + i).style(Decoration.bold.fg(Color.yellow)));
                table.row("Name", constructors[i].getName());
                table.row("Description", constructors[i].getDescription());
            }
        }
    }
