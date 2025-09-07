    @Override
    public void process(CommandProcess process) {
        if (!verifyOptions(process)) {
            return;
        }

        // 确认输入
        if (file == null) {
            if (this.input != null) {
                file = input;
            } else {
                process.end(-1, ": No file, nor input");
                return;
            }
        }

        File f = new File(file);
        if (!f.exists()) {
            process.end(-1, file + ": No such file or directory");
            return;
        }
        if (f.isDirectory()) {
            process.end(-1, file + ": Is a directory");
            return;
        }

        if (f.length() > sizeLimit) {
            process.end(-1, file + ": Is too large, size: " + f.length());
            return;
        }

        InputStream input = null;
        ByteBuf convertResult = null;

        try {
            input = new FileInputStream(f);
            byte[] bytes = IOUtils.getBytes(input);

            if (this.decode) {
                convertResult = Base64.decode(Unpooled.wrappedBuffer(bytes));
            } else {
                convertResult = Base64.encode(Unpooled.wrappedBuffer(bytes));
            }

            if (this.output != null) {
                int readableBytes = convertResult.readableBytes();
                OutputStream out = new FileOutputStream(this.output);
                convertResult.readBytes(out, readableBytes);
                process.appendResult(new Base64Model(null));
            } else {
                String base64Str = convertResult.toString(CharsetUtil.UTF_8);
                process.appendResult(new Base64Model(base64Str));
            }
        } catch (IOException e) {
            logger.error("read file error. name: " + file, e);
            process.end(1, "read file error: " + e.getMessage());
            return;
        } finally {
            if (convertResult != null) {
                convertResult.release();
            }
            IOUtils.close(input);
        }

        process.end();
    }
