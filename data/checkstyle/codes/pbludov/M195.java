    public static BitSet asBitSet(String... tokens) {
        return Arrays.stream(tokens)
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .mapToInt(TokenUtil::getTokenId)
                .collect(BitSet::new, BitSet::set, BitSet::or);
    }
