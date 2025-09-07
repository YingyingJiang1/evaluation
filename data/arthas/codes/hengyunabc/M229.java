    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated().anyRequest()
        .permitAll().and().formLogin();
        // allow iframe
        if (arthasProperties.isEnableIframeSupport()) {
            httpSecurity.headers().frameOptions().disable();
        }
    }
