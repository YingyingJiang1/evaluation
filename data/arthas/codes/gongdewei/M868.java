    private boolean verifyOptions(CommandProcess process) {
        if (sizeLimit > maxSizeLimit) {
            process.end(-1, "sizeLimit cannot be large than: " + maxSizeLimit);
            return false;
        }

        //目前不支持过滤，限制http请求执行的文件大小
        int maxSizeLimitOfNonTty = 128 * 1024;
        if (!process.session().isTty() && sizeLimit > maxSizeLimitOfNonTty) {
            process.end(-1, "When executing in non-tty session, sizeLimit cannot be large than: " + maxSizeLimitOfNonTty);
            return false;
        }
        return true;
    }
