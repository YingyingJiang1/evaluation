    private void drawMBeanNames(CommandProcess process, List<String> mbeanNames) {
        for (String mbeanName : mbeanNames) {
            process.write(mbeanName).write("\n");
        }
    }
