    @PreAuthorize("!hasAuthority('ROLE_DEMO_USER')")
    @PostMapping("/change-password")
    public RedirectView changePassword(
            Principal principal,
            @RequestParam(name = "currentPassword") String currentPassword,
            @RequestParam(name = "newPassword") String newPassword,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes)
            throws SQLException, UnsupportedProviderException {
        if (principal == null) {
            return new RedirectView("/account?messageType=notAuthenticated", true);
        }
        Optional<User> userOpt = userService.findByUsernameIgnoreCase(principal.getName());
        if (userOpt.isEmpty()) {
            return new RedirectView("/account?messageType=userNotFound", true);
        }
        User user = userOpt.get();
        if (!userService.isPasswordCorrect(user, currentPassword)) {
            return new RedirectView("/account?messageType=incorrectPassword", true);
        }
        userService.changePassword(user, newPassword);
        // Logout using Spring's utility
        new SecurityContextLogoutHandler().logout(request, response, null);
        return new RedirectView(LOGIN_MESSAGETYPE_CREDSUPDATED, true);
    }
