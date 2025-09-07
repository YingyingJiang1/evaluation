    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != ConvertiblePair.class) {
            return false;
        }
        ConvertiblePair other = (ConvertiblePair) obj;
        return this.sourceType.equals(other.sourceType) && this.targetType.equals(other.targetType);
    }
