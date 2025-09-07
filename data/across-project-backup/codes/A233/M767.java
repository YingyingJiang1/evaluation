    public List<SignatureFile> getAvailableSignatures(String username) {
        List<SignatureFile> signatures = new ArrayList<>();

        // Get signatures from user's personal folder
        if (!StringUtils.isEmptyOrWhitespace(username)) {
            Path userFolder = Paths.get(SIGNATURE_BASE_PATH, username);
            if (Files.exists(userFolder)) {
                try {
                    signatures.addAll(getSignaturesFromFolder(userFolder, "Personal"));
                } catch (IOException e) {
                    log.error("Error reading user signatures folder", e);
                }
            }
        }

        // Get signatures from ALL_USERS folder
        Path allUsersFolder = Paths.get(SIGNATURE_BASE_PATH, ALL_USERS_FOLDER);
        if (Files.exists(allUsersFolder)) {
            try {
                signatures.addAll(getSignaturesFromFolder(allUsersFolder, "Shared"));
            } catch (IOException e) {
                log.error("Error reading shared signatures folder", e);
            }
        }

        return signatures;
    }
