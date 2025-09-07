    @Override
    @GrpcMethod(value = "unary")
    public ArthasUnittest.ArthasUnittestResponse unary(ArthasUnittest.ArthasUnittestRequest command) {
        ArthasUnittest.ArthasUnittestResponse.Builder builder = ArthasUnittest.ArthasUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        return builder.build();
    }
