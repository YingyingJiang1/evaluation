    @Scheduled(fixedRate = 60000)
    public void scanFolders() {
        Path watchedFolderPath = Paths.get(watchedFoldersDir).toAbsolutePath();
        if (!Files.exists(watchedFolderPath)) {
            try {
                Files.createDirectories(watchedFolderPath);
                log.info("Created directory: {}", watchedFolderPath);
            } catch (IOException e) {
                log.error("Error creating directory: {}", watchedFolderPath, e);
                return;
            }
        }

        try {
            Files.walkFileTree(
                    watchedFolderPath,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult preVisitDirectory(
                                Path dir, BasicFileAttributes attrs) {
                            try {
                                // Skip root directory and "processing" subdirectories
                                if (!dir.equals(watchedFolderPath) && !dir.endsWith("processing")) {
                                    handleDirectory(dir);
                                }
                            } catch (Exception e) {
                                log.error("Error handling directory: {}", dir, e);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path path, IOException exc) {
                            // Handle broken symlinks or inaccessible directories
                            log.error("Error accessing path: {}", path, exc);
                            return FileVisitResult.CONTINUE;
                        }
                    });
        } catch (IOException e) {
            log.error("Error walking through directory: {}", watchedFolderPath, e);
        }
    }
