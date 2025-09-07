        public Builder truststore(URL truststore, char[] truststorePassword) {

            Objects.requireNonNull(truststore, "Truststore must not be null");

            return truststore(Resource.from(truststore), truststorePassword);
        }
