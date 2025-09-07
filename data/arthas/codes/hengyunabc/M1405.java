    public static Response requestViaSocket(String path) {
        BufferedReader br = null;
        try {
            Socket s = new Socket(QOS_HOST, QOS_PORT);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("GET " + path + " HTTP/1.1");
            pw.println("Host: " + QOS_HOST + ":" + QOS_PORT);
            pw.println("");
            pw.flush();

            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            boolean start = false;
            while ((line = br.readLine()) != null) {
                if (start) {
                    sb.append(line).append("\n");
                }
                if (line.equals(QOS_RESPONSE_START_LINE)) {
                    start = true;
                }
            }
            String result = sb.toString().trim();
            return new Response(result);
        } catch (Exception e) {
            return new Response(e.getMessage(), false);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
