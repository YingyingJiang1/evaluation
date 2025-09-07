    public boolean isFileReadyForProcessing(Path path) {
        // 1. Check FileMonitor's ready list
        boolean isReady = readyForProcessingFiles.contains(path.toAbsolutePath());

        // 2. Check last modified timestamp
        if (!isReady) {
            try {
                long lastModified = Files.getLastModifiedTime(path).toMillis();
                long currentTime = System.currentTimeMillis();
                isReady = (currentTime - lastModified) > 5000;
            } catch (IOException e) {
                log.info("Timestamp check failed for {}", path, e);
            }
        }

        // 3. Direct file lock check
        if (isReady) {
            try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "rw");
                    FileChannel channel = raf.getChannel()) {
                // Try acquiring an exclusive lock
                FileLock lock = channel.tryLock();
                if (lock == null) {
                    isReady = false;
                } else {
                    lock.release();
                }
            } catch (IOException e) {
                log.info("File lock detected on {}", path);
                isReady = false;
            }
        }

        return isReady;
    }
