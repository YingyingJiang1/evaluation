    @Override
    public Subject login(Principal principal) throws LoginException {
        if (principal == null) {
            return null;
        }
        if (principal instanceof BasicPrincipal) {
            BasicPrincipal basicPrincipal = (BasicPrincipal) principal;
            if (basicPrincipal.getName().equals(username) && basicPrincipal.getPassword().equals(this.password)) {
                return subject;
            }
        }
        if (principal instanceof LocalConnectionPrincipal) {
            return subject;
        }

        return null;
    }
