    PipelineResult runPipelineAgainstFiles(List<Resource> outputFiles, PipelineConfig config)
            throws Exception {
        PipelineResult result = new PipelineResult();

        ByteArrayOutputStream logStream = new ByteArrayOutputStream();
        PrintStream logPrintStream = new PrintStream(logStream);
        boolean hasErrors = false;
        boolean filtersApplied = false;
        for (PipelineOperation pipelineOperation : config.getOperations()) {
            String operation = pipelineOperation.getOperation();
            boolean isMultiInputOperation = apiDocService.isMultiInput(operation);
            log.info(
                    "Running operation: {} isMultiInputOperation {}",
                    operation,
                    isMultiInputOperation);
            Map<String, Object> parameters = pipelineOperation.getParameters();
            List<String> inputFileTypes = apiDocService.getExtensionTypes(false, operation);
            if (inputFileTypes == null) {
                inputFileTypes = new ArrayList<String>(Arrays.asList("ALL"));
            }
            if (!operation.matches("^[a-zA-Z0-9_-]+$")) {
                throw new IllegalArgumentException("Invalid operation value received.");
            }
            String url = getBaseUrl() + operation;
            List<Resource> newOutputFiles = new ArrayList<>();
            if (!isMultiInputOperation) {
                for (Resource file : outputFiles) {
                    boolean hasInputFileType = false;
                    for (String extension : inputFileTypes) {
                        if ("ALL".equals(extension)
                                || file.getFilename().toLowerCase().endsWith(extension)) {
                            hasInputFileType = true;
                            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                            body.add("fileInput", file);
                            for (Entry<String, Object> entry : parameters.entrySet()) {
                                if (entry.getValue() instanceof List<?> entryList) {
                                    for (Object item : entryList) {
                                        body.add(entry.getKey(), item);
                                    }
                                } else {
                                    body.add(entry.getKey(), entry.getValue());
                                }
                            }
                            ResponseEntity<byte[]> response = sendWebRequest(url, body);
                            // If the operation is filter and the response body is null or empty,
                            // skip
                            // this
                            // file
                            if (operation.startsWith("filter-")
                                    && (response.getBody() == null
                                            || response.getBody().length == 0)) {
                                filtersApplied = true;
                                log.info("Skipping file due to filtering {}", operation);
                                continue;
                            }
                            if (!HttpStatus.OK.equals(response.getStatusCode())) {
                                logPrintStream.println("Error: " + response.getBody());
                                hasErrors = true;
                                continue;
                            }
                            processOutputFiles(operation, response, newOutputFiles);
                        }
                    }
                    if (!hasInputFileType) {
                        String filename = file.getFilename();
                        String providedExtension = "no extension";
                        if (filename != null && filename.contains(".")) {
                            providedExtension =
                                    filename.substring(filename.lastIndexOf(".")).toLowerCase();
                        }

                        logPrintStream.println(
                                "No files with extension "
                                        + String.join(", ", inputFileTypes)
                                        + " found for operation "
                                        + operation
                                        + ". Provided file '"
                                        + filename
                                        + "' has extension: "
                                        + providedExtension);
                        hasErrors = true;
                    }
                }
            } else {
                // Filter and collect all files that match the inputFileExtension
                List<Resource> matchingFiles;
                if (inputFileTypes.contains("ALL")) {
                    matchingFiles = new ArrayList<>(outputFiles);
                } else {
                    final List<String> finalinputFileTypes = inputFileTypes;
                    matchingFiles =
                            outputFiles.stream()
                                    .filter(
                                            file ->
                                                    finalinputFileTypes.stream()
                                                            .anyMatch(
                                                                    file.getFilename().toLowerCase()
                                                                            ::endsWith))
                                    .toList();
                }
                // Check if there are matching files
                if (!matchingFiles.isEmpty()) {
                    // Create a new MultiValueMap for the request body
                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                    // Add all matching files to the body
                    for (Resource file : matchingFiles) {
                        body.add("fileInput", file);
                    }
                    for (Entry<String, Object> entry : parameters.entrySet()) {
                        if (entry.getValue() instanceof List<?> entryList) {
                            for (Object item : entryList) {
                                body.add(entry.getKey(), item);
                            }
                        } else {
                            body.add(entry.getKey(), entry.getValue());
                        }
                    }
                    ResponseEntity<byte[]> response = sendWebRequest(url, body);
                    // Handle the response
                    if (HttpStatus.OK.equals(response.getStatusCode())) {
                        processOutputFiles(operation, response, newOutputFiles);
                    } else {
                        // Log error if the response status is not OK
                        logPrintStream.println(
                                "Error in multi-input operation: " + response.getBody());
                        hasErrors = true;
                    }
                } else {
                    // Get details about what files were actually provided
                    List<String> providedExtensions =
                            outputFiles.stream()
                                    .map(
                                            file -> {
                                                String filename = file.getFilename();
                                                if (filename != null && filename.contains(".")) {
                                                    return filename.substring(
                                                                    filename.lastIndexOf("."))
                                                            .toLowerCase();
                                                }
                                                return "no extension";
                                            })
                                    .distinct()
                                    .toList();

                    logPrintStream.println(
                            "No files with extension "
                                    + String.join(", ", inputFileTypes)
                                    + " found for multi-input operation "
                                    + operation
                                    + ". Provided files have extensions: "
                                    + String.join(", ", providedExtensions)
                                    + " (total files: "
                                    + outputFiles.size()
                                    + ")");
                    hasErrors = true;
                }
            }
            logPrintStream.close();
            outputFiles = newOutputFiles;
        }
        if (hasErrors) {
            log.error("Errors occurred during processing. Log: {}", logStream.toString());
        }
        result.setHasErrors(hasErrors);
        result.setFiltersApplied(filtersApplied);
        result.setOutputFiles(outputFiles);
        return result;
    }
