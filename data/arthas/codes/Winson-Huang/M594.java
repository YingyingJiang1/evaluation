    @Option(longName = "title")
    @Description("FlameGraph title")
    public void setTitle(String title) {
        // escape HTML special characters
        // and escape comma to avoid conflicts with JVM TI
        title = title.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;")
                .replace(",", "&#44;");
        this.title = title;
    }
