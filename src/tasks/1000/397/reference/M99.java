    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!rateLimit) {
            // If rateLimit is not enabled, just pass all requests without rate limiting
            filterChain.doFilter(request, response);
            return;
        }
        String method = request.getMethod();
        if (!"POST".equalsIgnoreCase(method)) {
            // If the request is not a POST, just pass it through without rate limiting
            filterChain.doFilter(request, response);
            return;
        }
        String identifier = null;
        // Check for API key in the request headers
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            identifier = // Prefix to distinguish between API keys and usernames
                    "API_KEY_" + apiKey;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                identifier = userDetails.getUsername();
            }
        }
        // If neither API key nor an authenticated user is present, use IP address
        if (identifier == null) {
            identifier = request.getRemoteAddr();
        }
        Role userRole =
                getRoleFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
        if (request.getHeader("X-API-KEY") != null) {
            // It's an API call
            processRequest(
                    userRole.getApiCallsPerDay(),
                    identifier,
                    apiBuckets,
                    request,
                    response,
                    filterChain);
        } else {
            // It's a Web UI call
            processRequest(
                    userRole.getWebCallsPerDay(),
                    identifier,
                    webBuckets,
                    request,
                    response,
                    filterChain);
        }
    }
