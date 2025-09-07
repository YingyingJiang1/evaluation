    public String statusLine(Job job, ExecStatus status) {
        StringBuilder sb = new StringBuilder("[").append(job.id()).append("]");
        if (this.session().equals(job.getSession())) {
            sb.append("*");
        }
        sb.append("\n");
        sb.append("       ").append(Character.toUpperCase(status.name().charAt(0)))
                .append(status.name().substring(1).toLowerCase());
        sb.append("           ").append(job.line()).append("\n");
        sb.append("       execution count : ").append(job.process().times()).append("\n");
        sb.append("       start time      : ").append(job.process().startTime()).append("\n");
        String cacheLocation = job.process().cacheLocation();
        if (cacheLocation != null) {
            sb.append("       cache location  : ").append(cacheLocation).append("\n");
        }
        Date timeoutDate = job.timeoutDate();
        if (timeoutDate != null) {
            sb.append("       timeout date    : ").append(timeoutDate).append("\n");
        }
        sb.append("       session         : ").append(job.getSession().getSessionId()).append(
                session.equals(job.getSession()) ? " (current)" : "").append("\n");
        return sb.toString();
    }
