    @Option(shortName = "f", longName = "filename")
    @Description("Resulting recording filename, e.g. /tmp/MyRecording.jfr.")
    public void setFilename(String filename) {
        this.filename = filename;
    }
