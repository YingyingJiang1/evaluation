            @Override
            public void run() {
                int ch;

                try {
                    while (!interrupted() && (ch = localInput.read()) != -1) {
                        remoteOutput.write(ch);
                        remoteOutput.flush();
                    }
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
