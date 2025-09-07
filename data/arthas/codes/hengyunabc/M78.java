            @Override
            public void run() {
                try {
                    InputStreamReader reader = new InputStreamReader(remoteInput);
                    while (true) {
                        int singleChar = reader.read();
                        if (singleChar == -1) {
                            break;
                        }
                        localOutput.write(singleChar);
                        localOutput.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
