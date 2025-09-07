        public SslOptions build() {
            if (this.sslParameters == null) {
                this.sslParameters = new SSLParameters();
            }
            return new SslOptions(this);
        }
