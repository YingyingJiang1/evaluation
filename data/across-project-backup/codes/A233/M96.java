    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpServletRequest) {
            HttpServletRequest httpRequest = httpServletRequest;
            String method = httpRequest.getMethod();
            String requestURI = httpRequest.getRequestURI();
            // Check if the request is for static resources
            boolean isStaticResource =
                    RequestUriUtils.isStaticResource(httpRequest.getContextPath(), requestURI);

            // If it's a static resource, just continue the filter chain and skip the logic below
            if (isStaticResource) {
                chain.doFilter(request, response);
                return;
            }

            String clientIp = request.getRemoteAddr();
            requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
            if (!"GET".equalsIgnoreCase(method)) {

                if (requestCounts.get(clientIp).incrementAndGet() > maxRequests) {
                    // Handle limit exceeded (e.g., send error response)
                    response.getWriter().write("Rate limit exceeded");
                    return;
                }
            } else {
                if (requestCounts.get(clientIp).incrementAndGet() > maxGetRequests) {
                    // Handle limit exceeded (e.g., send error response)
                    response.getWriter().write("GET Rate limit exceeded");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
