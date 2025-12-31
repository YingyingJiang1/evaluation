    @PostMapping(value = "/handleData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> handleData(@ModelAttribute HandleDataRequest request)
            throws JsonMappingException, JsonProcessingException {
        MultipartFile[] files = request.getFileInput();
        String jsonString = request.getJson();
        if (files == null) {
            return null;
        }
        PipelineConfig config = objectMapper.readValue(jsonString, PipelineConfig.class);
        log.info("Received POST request to /handleData with {} files", files.length);

        List<String> operationNames =
                config.getOperations().stream().map(PipelineOperation::getOperation).toList();

        Map<String, Object> properties = new HashMap<>();
        properties.put("operations", operationNames);
        properties.put("fileCount", files.length);

        postHogService.captureEvent("pipeline_api_event", properties);

        try {
            List<Resource> inputFiles = processor.generateInputFiles(files);
            if (inputFiles == null || inputFiles.size() == 0) {
                return null;
            }
            PipelineResult result = processor.runPipelineAgainstFiles(inputFiles, config);
            List<Resource> outputFiles = result.getOutputFiles();
            if (outputFiles != null && outputFiles.size() == 1) {
                // If there is only one file, return it directly
                Resource singleFile = outputFiles.get(0);
                InputStream is = singleFile.getInputStream();
                byte[] bytes = new byte[(int) singleFile.contentLength()];
                is.read(bytes);
                is.close();
                log.info("Returning single file response...");
                return WebResponseUtils.bytesToWebResponse(
                        bytes, singleFile.getFilename(), MediaType.APPLICATION_OCTET_STREAM);
            } else if (outputFiles == null) {
                return null;
            }
            // Create a ByteArrayOutputStream to hold the zip
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(baos);
            // A map to keep track of filenames and their counts
            Map<String, Integer> filenameCount = new HashMap<>();
            // Loop through each file and add it to the zip
            for (Resource file : outputFiles) {
                String originalFilename = file.getFilename();
                String filename = originalFilename;
                // Check if the filename already exists, and modify it if necessary
                if (filenameCount.containsKey(originalFilename)) {
                    int count = filenameCount.get(originalFilename);
                    String baseName = originalFilename.replaceAll("\\.[^.]*$", "");
                    String extension = originalFilename.replaceAll("^.*\\.", "");
                    filename = baseName + "(" + count + ")." + extension;
                    filenameCount.put(originalFilename, count + 1);
                } else {
                    filenameCount.put(originalFilename, 1);
                }
                ZipEntry zipEntry = new ZipEntry(filename);
                zipOut.putNextEntry(zipEntry);
                // Read the file into a byte array
                InputStream is = file.getInputStream();
                byte[] bytes = new byte[(int) file.contentLength()];
                is.read(bytes);
                // Write the bytes of the file to the zip
                zipOut.write(bytes, 0, bytes.length);
                zipOut.closeEntry();
                is.close();
            }
            zipOut.close();
            log.info("Returning zipped file response...");
            return WebResponseUtils.baosToWebResponse(
                    baos, "output.zip", MediaType.APPLICATION_OCTET_STREAM);
        } catch (Exception e) {
            log.error("Error handling data: ", e);
            return null;
        }
    }
