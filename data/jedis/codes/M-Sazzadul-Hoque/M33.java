  private T toObject(byte[] data) {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais)) {
      return (T) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new JedisCacheException("Failed to deserialize object", e);
    }
  }
