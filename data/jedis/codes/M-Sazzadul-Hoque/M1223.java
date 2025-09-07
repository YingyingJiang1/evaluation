        public Builder keystore(URL keystore, char[] keystorePassword) {

            Objects.requireNonNull(keystore, "Keystore must not be null");

            return keystore(Resource.from(keystore), keystorePassword);
        }
