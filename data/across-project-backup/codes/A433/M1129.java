    public static Postprocessing getAlgorithm(@NonNull String algorithmName, String[] args) {
        Postprocessing instance;

        switch (algorithmName) {
            case ALGORITHM_TTML_CONVERTER:
                instance = new TtmlConverter();
                break;
            case ALGORITHM_WEBM_MUXER:
                instance = new WebMMuxer();
                break;
            case ALGORITHM_MP4_FROM_DASH_MUXER:
                instance = new Mp4FromDashMuxer();
                break;
            case ALGORITHM_M4A_NO_DASH:
                instance = new M4aNoDash();
                break;
            case ALGORITHM_OGG_FROM_WEBM_DEMUXER:
                instance = new OggFromWebmDemuxer();
                break;
            /*case "example-algorithm":
                instance = new ExampleAlgorithm();*/
            default:
                throw new UnsupportedOperationException("Unimplemented post-processing algorithm: " + algorithmName);
        }

        instance.args = args;
        return instance;
    }
