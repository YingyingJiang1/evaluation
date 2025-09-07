    @Override
    @GrpcMethod(value = "unaryAddSum")
    public ArthasUnittest.ArthasUnittestResponse unaryAddSum(ArthasUnittest.ArthasUnittestRequest command) {
        ArthasUnittest.ArthasUnittestResponse.Builder builder = ArthasUnittest.ArthasUnittestResponse.newBuilder();
        builder.setMessage(command.getMessage());
        map.merge(command.getId(), command.getNum(), Integer::sum);
        return builder.build();
    }
