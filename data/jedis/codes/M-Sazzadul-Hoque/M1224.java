        public Builder keystore(Resource resource, char[] keystorePassword) {

            this.keystoreResource = Objects.requireNonNull(resource, "Keystore InputStreamProvider must not be null");

            this.keystorePassword = getPassword(keystorePassword);

            return this;
        }
