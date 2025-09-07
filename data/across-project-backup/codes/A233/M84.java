    private User saveUserCore(
            String username,
            String password,
            AuthenticationType authenticationType,
            Long teamId,
            Team team,
            String role,
            boolean firstLogin,
            boolean enabled)
            throws IllegalArgumentException, SQLException, UnsupportedProviderException {

        if (!isUsernameValid(username)) {
            throw new IllegalArgumentException(getInvalidUsernameMessage());
        }

        User user = new User();
        user.setUsername(username);

        // Set password if provided
        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        // Set authentication type
        user.setAuthenticationType(authenticationType);

        // Set enabled status
        user.setEnabled(enabled);

        // Set first login flag
        user.setFirstLogin(firstLogin);

        // Set role (authority)
        if (role == null) {
            role = Role.USER.getRoleId();
        }
        user.addAuthority(new Authority(role, user));

        // Resolve and set team
        if (team != null) {
            user.setTeam(team);
        } else {
            user.setTeam(resolveTeam(teamId, this::getDefaultTeam));
        }

        // Save user
        userRepository.save(user);

        // Export database
        databaseService.exportDatabase();

        return user;
    }
