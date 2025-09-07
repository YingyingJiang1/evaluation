    @Override
    @GrpcMethod(value = "unaryGetSum")
    public ArthasUnittest.ArthasUnittestResponse unaryGetSum(ArthasUnittest.ArthasUnittestRequest command) {
        ArthasUnittest.ArthasUnittestResponse.Builder builder = ArthasUnittest.ArthasUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        Integer sum = map.getOrDefault(command.getId(), 0);
        builder.setNum(sum);
        return builder.build();
    }
