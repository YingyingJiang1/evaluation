        @Override
        public void onTerminated(Job job) {
            if (!job.isRunInBackground()){
                resetAndReadLine();
            }

            // save command history
            Term term = shell.term();
            if (term instanceof TermImpl) {
                List<int[]> history = ((TermImpl) term).getReadline().getHistory();
                FileUtils.saveCommandHistory(history, new File(Constants.CMD_HISTORY_FILE));
            }
        }
