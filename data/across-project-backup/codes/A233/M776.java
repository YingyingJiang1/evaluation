    private void loadMozillaCertificates() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/certdata.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder certData = new StringBuilder();
            boolean inCert = false;
            int certCount = 0;

            while ((line = BoundedLineReader.readLine(reader, 5_000_000)) != null) {
                if (line.startsWith("CKA_VALUE MULTILINE_OCTAL")) {
                    inCert = true;
                    certData = new StringBuilder();
                    continue;
                }
                if (inCert) {
                    if ("END".equals(line)) {
                        inCert = false;
                        byte[] certBytes = parseOctalData(certData.toString());
                        if (certBytes != null) {
                            CertificateFactory cf = CertificateFactory.getInstance("X.509");
                            X509Certificate cert =
                                    (X509Certificate)
                                            cf.generateCertificate(
                                                    new ByteArrayInputStream(certBytes));
                            trustStore.setCertificateEntry("mozilla-cert-" + certCount++, cert);
                        }
                    } else {
                        certData.append(line).append("\n");
                    }
                }
            }
        }
    }
