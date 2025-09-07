    private void addMatchedPosition(List<MatchedPosition> matchedPositions, Object o) {
      List<Object> matches = (List<Object>) o;
      for (Object obj : matches) {
        if (obj instanceof List<?>) {
          List<Object> positions = (List<Object>) obj;
          Position a = new Position(
              LONG.build(((List<Object>) positions.get(0)).get(0)),
              LONG.build(((List<Object>) positions.get(0)).get(1))
          );
          Position b = new Position(
              LONG.build(((List<Object>) positions.get(1)).get(0)),
              LONG.build(((List<Object>) positions.get(1)).get(1))
          );
          long matchLen = 0;
          if (positions.size() >= 3) {
            matchLen = LONG.build(positions.get(2));
          }
          matchedPositions.add(new MatchedPosition(a, b, matchLen));
        }
      }
    }
