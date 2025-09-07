  @Override
  public byte[] memoryDoctorBinary() {
    checkIsInMultiOrPipeline();
    connection.sendCommand(MEMORY, DOCTOR);
    return connection.getBinaryBulkReply();
  }
