    public VectorField build() {
      if (fieldName == null || algorithm == null || attributes == null || attributes.isEmpty()) {
        throw new IllegalArgumentException("All required VectorField parameters are not set.");
      }
      return new VectorField(fieldName, algorithm, attributes);
    }
