    private void drawNotificationInfo(MBeanNotificationInfo[] notificationInfos, TableElement table) {
        for (MBeanNotificationInfo notificationInfo : notificationInfos) {
            table.row(new LabelElement("MBeanNotificationInfo").style(Decoration.bold.fg(Color.red)));
            table.row(new LabelElement("Notification:").style(Decoration.bold.fg(Color.yellow)));
            table.row("Name", notificationInfo.getName());
            table.row("Description", notificationInfo.getDescription());
            table.row("NotifTypes", Arrays.toString(notificationInfo.getNotifTypes()));
            drawDescriptorInfo("Notification Descriptor:", notificationInfo, table);
        }
    }
