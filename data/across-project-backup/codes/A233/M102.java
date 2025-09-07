    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String[] permitAllPatterns = {
            contextPath + "/login",
            contextPath + "/register",
            contextPath + "/error",
            contextPath + "/images/",
            contextPath + "/public/",
            contextPath + "/css/",
            contextPath + "/fonts/",
            contextPath + "/js/",
            contextPath + "/pdfjs/",
            contextPath + "/pdfjs-legacy/",
            contextPath + "/api/v1/info/status",
            contextPath + "/site.webmanifest"
        };

        for (String pattern : permitAllPatterns) {
            if (uri.startsWith(pattern)
                    || uri.endsWith(".svg")
                    || uri.endsWith(".png")
                    || uri.endsWith(".ico")) {
                return true;
            }
        }

        return false;
    }
