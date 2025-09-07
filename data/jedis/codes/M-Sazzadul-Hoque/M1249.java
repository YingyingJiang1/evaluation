    public Builder addAttribute(String name, Object value) {
      if (this.attributes == null) {
        this.attributes = new LinkedHashMap<>();
      }
      this.attributes.put(name, value);
      return this;
    }
