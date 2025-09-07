        public Builder truststore(File truststore, char[] truststorePassword) {

            Objects.requireNonNull(truststore, "Truststore must not be null");
            assertFile("Truststore", truststore);

            return truststore(Resource.from(truststore), truststorePassword);
        }
