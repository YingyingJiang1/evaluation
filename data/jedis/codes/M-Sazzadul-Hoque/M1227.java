        public Builder truststore(Resource resource, char[] truststorePassword) {

            this.truststoreResource = Objects.requireNonNull(resource, "Truststore InputStreamProvider must not be null");

            this.truststorePassword = getPassword(truststorePassword);

            return this;
        }
