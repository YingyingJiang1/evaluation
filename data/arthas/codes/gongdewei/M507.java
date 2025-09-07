    private void drawMBeanMetadata(CommandProcess process, Map<String, MBeanInfo> mbeanMetadata) {
        TableElement table = createTable();
        for (Map.Entry<String, MBeanInfo> entry : mbeanMetadata.entrySet()) {
            String objectName = entry.getKey();
            MBeanInfo mBeanInfo = entry.getValue();
            drawMetaInfo(mBeanInfo, objectName, table);
            drawAttributeInfo(mBeanInfo.getAttributes(), table);
            drawOperationInfo(mBeanInfo.getOperations(), table);
            drawNotificationInfo(mBeanInfo.getNotifications(), table);
        }
        process.write(RenderUtil.render(table, process.width()));

    }
