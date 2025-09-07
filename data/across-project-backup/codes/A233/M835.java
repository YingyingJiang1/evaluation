    @PostConstruct
    public void checkDependencies() {
        // Check core dependencies
        checkDependencyAndDisableGroup("gs");
        checkDependencyAndDisableGroup("ocrmypdf");
        checkDependencyAndDisableGroup("tesseract");
        checkDependencyAndDisableGroup("soffice");
        checkDependencyAndDisableGroup("qpdf");
        checkDependencyAndDisableGroup(weasyprintPath);
        checkDependencyAndDisableGroup("pdftohtml");
        checkDependencyAndDisableGroup(unoconvPath);
        // Special handling for Python/OpenCV dependencies
        boolean pythonAvailable = isCommandAvailable("python3") || isCommandAvailable("python");
        if (!pythonAvailable) {
            List<String> pythonFeatures = getAffectedFeatures("Python");
            List<String> openCVFeatures = getAffectedFeatures("OpenCV");
            endpointConfiguration.disableGroup("Python");
            endpointConfiguration.disableGroup("OpenCV");
            log.warn(
                    "Missing dependency: Python - Disabling Python features: {} and OpenCV features: {}",
                    String.join(", ", pythonFeatures),
                    String.join(", ", openCVFeatures));
        } else {
            // If Python is available, check for OpenCV
            try {
                ProcessBuilder processBuilder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    processBuilder.command("python", "-c", "import cv2");
                } else {
                    processBuilder.command("python3", "-c", "import cv2");
                }
                Process process = processBuilder.start();
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    List<String> openCVFeatures = getAffectedFeatures("OpenCV");
                    endpointConfiguration.disableGroup("OpenCV");
                    log.warn(
                            "OpenCV not available in Python - Disabling OpenCV features: {}",
                            String.join(", ", openCVFeatures));
                }
            } catch (Exception e) {
                List<String> openCVFeatures = getAffectedFeatures("OpenCV");
                endpointConfiguration.disableGroup("OpenCV");
                log.warn(
                        "Error checking OpenCV: {} - Disabling OpenCV features: {}",
                        e.getMessage(),
                        String.join(", ", openCVFeatures));
            }
        }
        endpointConfiguration.logDisabledEndpointsSummary();
    }
