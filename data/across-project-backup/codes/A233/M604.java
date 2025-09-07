    @PostMapping(value = "/form-fields", consumes = "multipart/form-data")
    @Operation(
            summary = "Get form field information",
            description =
                    "Returns count and details of form fields. Input:PDF Output:JSON Type:SISO")
    public Map<String, Object> getFormFields(@ModelAttribute PDFFile file) throws IOException {
        try (PDDocument document = pdfDocumentFactory.load(file.getFileInput())) {
            Map<String, Object> formInfo = new HashMap<>();
            PDAcroForm form = document.getDocumentCatalog().getAcroForm();

            if (form != null) {
                formInfo.put("fieldCount", form.getFields().size());
                formInfo.put("hasXFA", form.hasXFA());
                formInfo.put("isSignaturesExist", form.isSignaturesExist());
            } else {
                formInfo.put("fieldCount", 0);
                formInfo.put("hasXFA", false);
                formInfo.put("isSignaturesExist", false);
            }
            return formInfo;
        }
    }
