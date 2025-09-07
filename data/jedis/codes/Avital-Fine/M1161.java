  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof GeoRadiusResponse)) {
      return false;
    }

    GeoRadiusResponse response = (GeoRadiusResponse) obj;
    return Double.compare(distance, response.getDistance()) == 0
            && rawScore == response.getRawScore() && coordinate.equals(response.coordinate)
            && Arrays.equals(member, response.getMember());
  }
