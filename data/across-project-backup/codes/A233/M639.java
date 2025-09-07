    private List<Resource> processOutputFiles(
            String operation, ResponseEntity<byte[]> response, List<Resource> newOutputFiles)
            throws IOException {
        // Define filename
        String newFilename;
        if (operation.contains("auto-rename")) {
            // If the operation is "auto-rename", generate a new filename.
            // This is a simple example of generating a filename using current timestamp.
            // Modify as per your needs.
            newFilename = extractFilename(response);
        } else {
            // Otherwise, keep the original filename.
            newFilename = removeTrailingNaming(extractFilename(response));
        }
        // Check if the response body is a zip file
        if (isZip(response.getBody())) {
            // Unzip the file and add all the files to the new output files
            newOutputFiles.addAll(unzip(response.getBody()));
        } else {
            Resource outputResource =
                    new ByteArrayResource(response.getBody()) {

                        @Override
                        public String getFilename() {
                            return newFilename;
                        }
                    };
            newOutputFiles.add(outputResource);
        }
        return newOutputFiles;
    }
