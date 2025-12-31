   private void extractEndpoints() {
       int count = 1;
       while (true) {
           String e = cfg.getProperty(
                   String.format("rest.endpoint.%d", count), null);
           if (e == null) {
               break;
           }

           String[] parts = e.split(";");
           if (parts.length != 2) {
               count++;
               continue;
           }
           Endpoint point = new Endpoint(parts[0], parts[1]);
           
           String c = cfg.getProperty(String.format(
                   "rest.endpoint.%d.http.auth", count), "");
           point.setCredentials(c);
           
           String digest = cfg.getProperty(String.format(
                   "rest.endpoint.%d.zk.digest", count), "");
           point.setZooKeeperAuthInfo(digest);

           endpoints.add(point);
           count++;
       }
   }
