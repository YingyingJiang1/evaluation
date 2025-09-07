        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof ByteArrayWrapper)) return false;

            return Arrays.equals(data, ((ByteArrayWrapper) other).data);
        }
