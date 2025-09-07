    @PostMapping(value = "/annotation-info", consumes = "multipart/form-data")
    @Operation(
            summary = "Get annotation information",
            description = "Returns count and types of annotations. Input:PDF Output:JSON Type:SISO")
    public Map<String, Object> getAnnotationInfo(@ModelAttribute PDFFile file) throws IOException {
        try (PDDocument document = pdfDocumentFactory.load(file.getFileInput())) {
            Map<String, Object> annotInfo = new HashMap<>();
            int totalAnnotations = 0;
            Map<String, Integer> annotationTypes = new HashMap<>();

            for (PDPage page : document.getPages()) {
                for (PDAnnotation annot : page.getAnnotations()) {
                    totalAnnotations++;
                    String subType = annot.getSubtype();
                    annotationTypes.merge(subType, 1, Integer::sum);
                }
            }

            annotInfo.put("totalCount", totalAnnotations);
            annotInfo.put("typeBreakdown", annotationTypes);
            return annotInfo;
        }
    }
