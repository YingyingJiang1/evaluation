        public Builder keystore(File keystore, char[] keystorePassword) {

            Objects.requireNonNull(keystore, "Keystore must not be null");
            assertFile("Keystore", keystore);

            return keystore(Resource.from(keystore), keystorePassword);
        }
