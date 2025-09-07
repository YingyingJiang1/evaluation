        private void resetAndReadLine() {
            //reset stdin handler to echo handler
            //shell.term().stdinHandler(null);
            shell.setForegroundJob(null);
            shell.readline();
        }
