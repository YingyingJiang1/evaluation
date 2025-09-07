            @Override
            public void run() {
                try {
                    URLConnection urlConnection = openURLConnection(ARTHAS_LATEST_VERSIONS_URL);
                    InputStream inputStream = urlConnection.getInputStream();
                    version[0] = com.taobao.arthas.common.IOUtils.toString(inputStream).trim();
                } catch (Throwable e) {
                    logger.debug("get latest version error", e);
                }
            }
