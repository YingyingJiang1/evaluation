    public void start() {
        GrpcResultViewResolver grpcResultViewResolver = new GrpcResultViewResolver();
        GrpcJobController grpcJobController = new GrpcJobController(this.instrumentation, this.transformerManager, grpcResultViewResolver);
        File path = new File(VmTool.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
        String libPath = path.getAbsolutePath();

        try {
            grpcServer = ServerBuilder.forPort(port)
                    .addService(new ObjectService(grpcJobController,libPath))
                    .addService(new PwdCommandService(grpcJobController))
                    .addService(new SystemPropertyCommandService(grpcJobController))
                    .addService(new WatchCommandService(grpcJobController))
                    .build()
                    .start();
            logger.info("Server started, listening on " + port);
            Runtime.getRuntime().addShutdownHook(new Thread("grpc-server-shutdown") {
                @Override
                public void run() {
                    if (grpcServer != null) {
                        grpcServer.shutdown();
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
