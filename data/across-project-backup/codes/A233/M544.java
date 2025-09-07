    private void applyQpdfCompression(
            OptimizePdfRequest request, int optimizeLevel, Path currentFile, List<Path> tempFiles)
            throws IOException {

        long preQpdfSize = Files.size(currentFile);
        log.info("Pre-QPDF file size: {}", GeneralUtils.formatBytes(preQpdfSize));

        // Map optimization levels to QPDF compression levels
        int qpdfCompressionLevel;
        if (optimizeLevel == 1) {
            qpdfCompressionLevel = 5;
        } else if (optimizeLevel == 2) {
            qpdfCompressionLevel = 9;
        } else {
            qpdfCompressionLevel = 9;
        }

        // Create output file for QPDF
        Path qpdfOutputFile = Files.createTempFile("qpdf_output_", ".pdf");
        tempFiles.add(qpdfOutputFile);

        // Build QPDF command
        List<String> command = new ArrayList<>();
        command.add("qpdf");
        if (request.getNormalize()) {
            command.add("--normalize-content=y");
        }
        if (request.getLinearize()) {
            command.add("--linearize");
        }
        command.add("--recompress-flate");
        command.add("--compression-level=" + qpdfCompressionLevel);
        command.add("--compress-streams=y");
        command.add("--object-streams=generate");
        command.add(currentFile.toString());
        command.add(qpdfOutputFile.toString());

        ProcessExecutorResult returnCode = null;
        try {
            returnCode =
                    ProcessExecutor.getInstance(ProcessExecutor.Processes.QPDF)
                            .runCommandWithOutputHandling(command);

            // Update current file to the QPDF output
            Files.copy(qpdfOutputFile, currentFile, StandardCopyOption.REPLACE_EXISTING);

            long postQpdfSize = Files.size(currentFile);
            double qpdfReduction = 100.0 - ((postQpdfSize * 100.0) / preQpdfSize);
            log.info(
                    "Post-QPDF file size: {} (reduced by {}%)",
                    GeneralUtils.formatBytes(postQpdfSize), String.format("%.1f", qpdfReduction));

        } catch (Exception e) {
            if (returnCode != null && returnCode.getRc() != 3) {
                throw new IOException("QPDF command failed", e);
            }
            // If QPDF fails, keep using the current file
            log.warn("QPDF compression failed, continuing with current file", e);
        }
    }
