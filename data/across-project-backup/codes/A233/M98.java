    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        // Check if the request is for static resources
        boolean isStaticResource = RequestUriUtils.isStaticResource(contextPath, requestURI);
        // If it's a static resource, just continue the filter chain and skip the logic below
        if (isStaticResource) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> user = userService.findByUsernameIgnoreCase(authentication.getName());
            if ("GET".equalsIgnoreCase(method)
                    && user.isPresent()
                    && user.get().isFirstLogin()
                    && !(contextPath + "/change-creds").equals(requestURI)) {
                response.sendRedirect(contextPath + "/change-creds");
                return;
            }
        }
        if (log.isDebugEnabled()) {
            HttpSession session = request.getSession(true);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String creationTime = timeFormat.format(new Date(session.getCreationTime()));
            log.debug(
                    "Request Info - New: {}, creationTimeSession {}, ID:  {}, IP: {}, User-Agent: {}, Referer: {}, Request URL: {}",
                    session.isNew(),
                    creationTime,
                    session.getId(),
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"),
                    request.getHeader("Referer"),
                    request.getRequestURL().toString());
        }
        filterChain.doFilter(request, response);
    }
