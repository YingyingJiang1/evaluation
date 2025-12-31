   public void start() throws IOException {
       System.out.println("Starting grizzly ...");

       boolean useSSL = cfg.useSSL();
       String zkRestResourcesTempPath = Files.createTempDirectory("zkRestResourcesTempPath").toFile().getCanonicalPath();
       gws = new GrizzlyWebServer(cfg.getPort(), zkRestResourcesTempPath, useSSL);
       // BUG: Grizzly needs a doc root if you are going to register multiple adapters

       for (Endpoint e : cfg.getEndpoints()) {
           ZooKeeperService.mapContext(e.getContext(), e);
           gws.addGrizzlyAdapter(createJerseyAdapter(e), new String[] { e
                   .getContext() });
       }
       
       if (useSSL) {
           System.out.println("Starting SSL ...");
           String jks = cfg.getJKS("keys/rest.jks");
           String jksPassword = cfg.getJKSPassword();

           SSLConfig sslConfig = new SSLConfig();
           URL resource = getClass().getClassLoader().getResource(jks);
           if (resource == null) {
               LOG.error("Unable to find the keystore file: " + jks);
               System.exit(2);
           }
           try {
               sslConfig.setKeyStoreFile(new File(resource.toURI())
                       .getAbsolutePath());
           } catch (URISyntaxException e1) {
               LOG.error("Unable to load keystore: " + jks, e1);
               System.exit(2);
           }
           sslConfig.setKeyStorePass(jksPassword);
           gws.setSSLConfig(sslConfig);
       }

       gws.start();
   }
