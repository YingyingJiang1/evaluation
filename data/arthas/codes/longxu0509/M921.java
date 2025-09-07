    @Override
    public void process(CommandProcess process) {

        if ("start".equals(cmd)) {
            Configuration c = null;
            try {
                if (getSettings() == null) {
                    setSettings("default");
                }
                c = Configuration.getConfiguration(settings);
            } catch (Throwable e) {
                process.end(-1, "Could not start recording, not able to read settings");
            }
            Recording r = new Recording(c);

            if (getFilename() != null) {
                try {
                    r.setDestination(Paths.get(getFilename()));
                } catch (IOException e) {
                    r.close();
                    process.end(-1, "Could not start recording, not able to write to file " + getFilename() + e.getMessage());
                }
            }

            if (getMaxSize() != null) {
                try {
                    r.setMaxSize(parseSize(getMaxSize()));
                } catch (Exception e) {
                    process.end(-1, e.getMessage());
                }
            }

            if (getMaxAge() != null) {
                try {
                    r.setMaxAge(Duration.ofNanos(parseTimespan(getMaxAge())));
                } catch (Exception e) {
                    process.end(-1, e.getMessage());
                }
            }

            if (isDumpOnExit() != false) {
                r.setDumpOnExit(isDumpOnExit().booleanValue());
            }

            if (getDuration() != null) {
                try {
                    r.setDuration(Duration.ofNanos(parseTimespan(getDuration())));
                } catch (Exception e) {
                    process.end(-1, e.getMessage());
                }
            }

            if (getName() == null) {
                r.setName("Recording-" + r.getId());
            } else {
                r.setName(getName());
            }

            long id = r.getId();
            recordings.put(id, r);

            if (getDelay() != null) {
                try {
                    r.scheduleStart(Duration.ofNanos(parseTimespan(getDelay())));
                } catch (Exception e) {
                    process.end(-1, e.getMessage());
                }
                result.setJfrOutput("Recording " + r.getId() + " scheduled to start in " + getDelay());
            } else {
                r.start();
                result.setJfrOutput("Started recording " + r.getId() + ".");
            }

            if (duration == null && maxAge == null && maxSize == null) {
                result.setJfrOutput(" No limit specified, using maxsize=250MB as default.");
                r.setMaxSize(250 * 1024L * 1024L);
            }

            if (filename != null && duration != null) {
                result.setJfrOutput(" The result will be written to:\n" + filename);
            }
        } else if ("status".equals(cmd)) {
            // list recording id = recording
            if (getRecording() != null) {
                Recording r = recordings.get(getRecording());
                if (r == null) {
                    process.end(-1, "recording not exit");
                }
                printRecording(r);
            } else {// list all recordings
                List<Recording> recordingList;
                if (state != null) {
                    recordingList = findRecordingByState(state);
                } else {
                    recordingList = new ArrayList<Recording>(recordings.values());
                }
                if (recordingList.isEmpty()) {
                    process.end(-1, "No available recordings.\n Use jfr start to start a recording.\n");
                } else {
                    for (Recording recording : recordingList) {
                        printRecording(recording);
                    }
                }
            }
        } else if ("dump".equals(cmd)) {
            if (recordings.isEmpty()) {
                process.end(-1, "No recordings to dump. Use jfr start to start a recording.");
            }
            if (getRecording() != null) {
                Recording r = recordings.get(getRecording());
                if (r == null) {
                    process.end(-1, "recording not exit");
                }
                if (getFilename() == null) {
                    try {
                        setFilename(outputFile());
                    } catch (IOException e) {
                        process.end(-1, e.getMessage());
                    }
                }

                try {
                    r.dump(Paths.get(getFilename()));
                } catch (IOException e) {
                    process.end(-1, "Could not to dump. " + e.getMessage());
                }
                result.setJfrOutput("Dump recording " + r.getId() + ", The result will be written to:\n" + getFilename());
            } else {
                process.end(-1, "Failed to dump. Please input recording id");
            }

        } else if ("stop".equals(cmd)) {
            if (recordings.isEmpty()) {
                process.end(-1, "No recordings to stop. Use jfr start to start a recording.");
            }
            if (getRecording() != null) {
                Recording r = recordings.remove(getRecording());
                if (r == null) {
                    process.end(-1, "recording not exit");
                }
                if ("CLOSED".equals(r.getState().toString()) || "STOPPED".equals(r.getState().toString())) {
                    process.end(-1, "Failed to stop recording, state can not be closed/stopped");
                }
                if (getFilename() == null) {
                    try {
                        setFilename(outputFile());
                    } catch (IOException e) {
                        process.end(-1, e.getMessage());
                    }
                }
                try {
                    r.setDestination(Paths.get(getFilename()));
                } catch (IOException e) {
                    process.end(-1, "Failed to stop " + r.getName() + ". Could not set destination for " + filename + "to file" + e.getMessage());
                }

                r.stop();
                result.setJfrOutput("Stop recording " + r.getId() + ", The result will be written to:\n" + getFilename());
                r.close();
            } else {
                process.end(-1, "Failed to stop. please input recording id");
            }
        } else {
            process.end(-1, "Please input correct jfr command (start status stop dump)");
        }

        process.appendResult(result);
        process.end();
    }
