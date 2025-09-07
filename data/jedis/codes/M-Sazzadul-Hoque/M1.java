    @Override
    public int compareTo(RedisVersion other) {
        int max = Math.max(this.numbers.length, other.numbers.length);
        for (int i = 0; i < max; i++) {
            int thisNumber = this.numbers.length > i ? this.numbers[i]:0;
            int otherNumber = other.numbers.length > i ? other.numbers[i]:0;
            if (thisNumber < otherNumber) return -1;
            if (thisNumber > otherNumber) return 1;
        }
        return 0;
    }
