  protected void checkIsInMultiOrPipeline() {
//    if (connection.isInMulti()) {
    if (transaction != null) {
      throw new IllegalStateException(
          "Cannot use Jedis when in Multi. Please use Transaction or reset jedis state.");
    } else if (pipeline != null && pipeline.hasPipelinedResponse()) {
      throw new IllegalStateException(
          "Cannot use Jedis when in Pipeline. Please use Pipeline or reset jedis state.");
    }
  }
