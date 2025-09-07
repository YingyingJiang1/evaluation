    public ArthasStreamObserver getGrpcJob(long jobId){
        if(this.containsJob(jobId)){
            return jobs.get(jobId);
        }else {
            return null;
        }
    }
