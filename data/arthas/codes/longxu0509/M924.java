    private String outputFile() throws IOException {
        if (this.filename == null) {
            File outputPath = ArthasBootstrap.getInstance().getOutputPath();
            if (outputPath != null) {
                this.filename = new File(outputPath,
                        new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".jfr")
                        .getAbsolutePath();
            } else {
                this.filename = File.createTempFile("arthas-output", ".jfr").getAbsolutePath();
            }
        }
        return filename;
    }
