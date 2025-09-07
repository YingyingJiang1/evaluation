    private String outputFileExt() {
        String fileExt = "";
        if (this.format == null) {
            fileExt = "html";
        } else if (this.format.startsWith("flat") || this.format.startsWith("traces") 
                || this.format.equals("collapsed")) {
            fileExt = "txt";
        } else if (this.format.equals("flamegraph") || this.format.equals("tree")) {
            fileExt = "html";
        } else if (this.format.equals("jfr")) {
            fileExt = "jfr";
        } else {
            // illegal -o option makes async-profiler use flat
            fileExt = "txt";
        }
        return fileExt;
    }
