    public static String styledUsage(CLI cli, int width) {
        if(cli == null) {
            return "";
        }
        StringBuilder usageBuilder = new StringBuilder();
        UsageMessageFormatter formatter = new StyledUsageFormatter(Color.green);
        formatter.setWidth(width);
        cli.usage(usageBuilder, formatter);
        return usageBuilder.toString();
    }
