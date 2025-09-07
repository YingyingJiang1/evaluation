    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreadVO threadVO = (ThreadVO) o;

        if (id != threadVO.id) return false;
        return name != null ? name.equals(threadVO.name) : threadVO.name == null;
    }
