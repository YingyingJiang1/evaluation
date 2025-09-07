    public Map<String, Object> extSessions() {
        if (context != null) {
            HttpSession httpSession = HttpSessionManager.getHttpSessionFromContext(context);
            if (httpSession != null) {
                Object subject = httpSession.getAttribute(ArthasConstants.SUBJECT_KEY);
                if (subject != null) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put(ArthasConstants.SUBJECT_KEY, subject);
                    return result;
                }
            }
        }
        return Collections.emptyMap();
    }
