        @Override
        public void onBackground(Job job) {
            if (session.getForegroundJob() == job) {
                session.setForegroundJob(null);
                updateSessionInputStatus(session, InputStatus.ALLOW_INPUT);
            }
        }
