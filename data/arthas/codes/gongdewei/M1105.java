        @Override
        public void onSuspend(Job job) {
            if (!job.isRunInBackground()){
                resetAndReadLine();
            }
        }
