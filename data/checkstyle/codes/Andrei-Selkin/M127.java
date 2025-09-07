        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            final Suppression suppression = (Suppression) other;
            return Objects.equals(lineNo, suppression.lineNo)
                    && Objects.equals(suppressionType, suppression.suppressionType)
                    && Objects.equals(eventSourceRegexp, suppression.eventSourceRegexp)
                    && Objects.equals(eventMessageRegexp, suppression.eventMessageRegexp)
                    && Objects.equals(eventIdRegexp, suppression.eventIdRegexp);
        }
