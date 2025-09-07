    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "No user found with username: " + username));
        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException(
                    "Your account has been locked due to too many failed login attempts.");
        }
        if (!user.hasPassword()) {
            throw new IllegalArgumentException("Password must not be null");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(user.getAuthorities()));
    }
