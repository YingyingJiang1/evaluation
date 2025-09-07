    @Override
    public boolean equals(Object other) {
        return (this == other
                || (other instanceof PropertySource && nullSafeEquals(this.name, ((PropertySource<?>) other).name)));
    }
