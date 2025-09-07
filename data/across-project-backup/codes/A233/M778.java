    public boolean validateCertificateChain(X509Certificate cert) {
        try {
            CertPathValidator validator = CertPathValidator.getInstance("PKIX");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            List<X509Certificate> certList = Arrays.asList(cert);
            CertPath certPath = cf.generateCertPath(certList);

            Set<TrustAnchor> anchors = new HashSet<>();
            Enumeration<String> aliases = trustStore.aliases();
            while (aliases.hasMoreElements()) {
                Object trustCert = trustStore.getCertificate(aliases.nextElement());
                if (trustCert instanceof X509Certificate x509Cert) {
                    anchors.add(new TrustAnchor(x509Cert, null));
                }
            }

            PKIXParameters params = new PKIXParameters(anchors);
            params.setRevocationEnabled(false);
            validator.validate(certPath, params);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
