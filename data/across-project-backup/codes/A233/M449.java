    private void setupDownloadHandler() {
        client.addDownloadHandler(
                new CefDownloadHandlerAdapter() {
                    @Override
                    public boolean onBeforeDownload(
                            CefBrowser browser,
                            CefDownloadItem downloadItem,
                            String suggestedName,
                            CefBeforeDownloadCallback callback) {
                        callback.Continue("", true);
                        return true;
                    }

                    @Override
                    public void onDownloadUpdated(
                            CefBrowser browser,
                            CefDownloadItem downloadItem,
                            CefDownloadItemCallback callback) {
                        if (downloadItem.isComplete()) {
                            log.info("Download completed: " + downloadItem.getFullPath());
                        } else if (downloadItem.isCanceled()) {
                            log.info("Download canceled: " + downloadItem.getFullPath());
                        }
                    }
                });
    }
