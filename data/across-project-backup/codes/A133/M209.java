    public void init() throws IllegalStateException {
        // 尝试判断arthas是否已在运行，如果是的话，直接就退出
        try {
            Class.forName("java.arthas.SpyAPI"); // 加载不到会抛异常
            if (SpyAPI.isInited()) {
                return;
            }
        } catch (Throwable e) {
            // ignore
        }

        try {
            if (instrumentation == null) {
                instrumentation = ByteBuddyAgent.install();
            }

            // 检查 arthasHome
            if (arthasHome == null || arthasHome.trim().isEmpty()) {
                // 解压出 arthasHome
                URL coreJarUrl = this.getClass().getClassLoader().getResource("arthas-bin.zip");
                if (coreJarUrl != null) {
                    File tempArthasDir = createTempDir();
                    ZipUtil.unpack(coreJarUrl.openStream(), tempArthasDir);
                    arthasHome = tempArthasDir.getAbsolutePath();
                } else {
                    throw new IllegalArgumentException("can not getResources arthas-bin.zip from classloader: "
                            + this.getClass().getClassLoader());
                }
            }

            // find arthas-core.jar
            File arthasCoreJarFile = new File(arthasHome, ARTHAS_CORE_JAR);
            if (!arthasCoreJarFile.exists()) {
                throw new IllegalStateException("can not find arthas-core.jar under arthasHome: " + arthasHome);
            }
            AttachArthasClassloader arthasClassLoader = new AttachArthasClassloader(
                    new URL[] { arthasCoreJarFile.toURI().toURL() });

            /**
             * <pre>
             * ArthasBootstrap bootstrap = ArthasBootstrap.getInstance(inst);
             * </pre>
             */
            Class<?> bootstrapClass = arthasClassLoader.loadClass(ARTHAS_BOOTSTRAP);
            Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class, Map.class).invoke(null,
                    instrumentation, configMap);
            boolean isBind = (Boolean) bootstrapClass.getMethod(IS_BIND).invoke(bootstrap);
            if (!isBind) {
                String errorMsg = "Arthas server port binding failed! Please check $HOME/logs/arthas/arthas.log for more details.";
                throw new RuntimeException(errorMsg);
            }
        } catch (Throwable e) {
            errorMessage = e.getMessage();
            if (!slientInit) {
                throw new IllegalStateException(e);
            }
        }
    }
