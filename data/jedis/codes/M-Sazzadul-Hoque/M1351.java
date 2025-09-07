    @Override
    public SearchResult build(Object data) {
      List<Object> resp = (List<Object>) data;

      int step = 1;
      int scoreOffset = 0;
      int contentOffset = 1;
      if (hasScores) {
        step += 1;
        scoreOffset = 1;
        contentOffset += 1;
      }
      if (hasContent) {
        step += 1;
      }

      // the first element is always the number of results
      long totalResults = (Long) resp.get(0);
      List<Document> documents = new ArrayList<>(resp.size() - 1);

      for (int i = 1; i < resp.size(); i += step) {

        String id = BuilderFactory.STRING.build(resp.get(i));
        double score = hasScores ? BuilderFactory.DOUBLE.build(resp.get(i + scoreOffset)) : 1.0;
        List<byte[]> fields = hasContent ? (List<byte[]>) resp.get(i + contentOffset) : null;

        documents.add(Document.load(id, score, fields, decode, isFieldDecode));
      }

      return new SearchResult(totalResults, documents);
    }
