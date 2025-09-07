    private String createOutputFileName(Resource resource, PipelineConfig config) {
        String resourceName = resource.getFilename();
        String baseName = resourceName.substring(0, resourceName.lastIndexOf('.'));
        String extension = resourceName.substring(resourceName.lastIndexOf('.') + 1);
        String outputFileName =
                config.getOutputPattern()
                                .replace("{filename}", baseName)
                                .replace("{pipelineName}", config.getName())
                                .replace(
                                        "{date}",
                                        LocalDate.now()
                                                .format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .replace(
                                        "{time}",
                                        LocalTime.now()
                                                .format(DateTimeFormatter.ofPattern("HHmmss")))
                        + "."
                        + extension;
        return outputFileName;
    }
