    private void runPipelineAgainstFiles(
            List<File> filesToProcess, PipelineConfig config, Path dir, Path processingDir)
            throws IOException {
        try {
            List<Resource> inputFiles =
                    processor.generateInputFiles(filesToProcess.toArray(new File[0]));
            if (inputFiles == null || inputFiles.isEmpty()) {
                return;
            }
            PipelineResult result = processor.runPipelineAgainstFiles(inputFiles, config);

            if (result.isHasErrors()) {
                log.error("Errors occurred during processing, retaining original files");
                moveToErrorDirectory(filesToProcess, dir);
            } else {
                moveAndRenameFiles(result.getOutputFiles(), config, dir);
                deleteOriginalFiles(filesToProcess, processingDir);
            }
        } catch (Exception e) {
            log.error("Error during processing", e);
            moveFilesBack(filesToProcess, processingDir);
        }
    }
