    private void updateMediaSessionActions() {
        // On Android 13+ (or Android T or API 33+) the actions in the player notification can't be
        // controlled directly anymore, but are instead derived from custom media session actions.
        // However the system allows customizing only two of these actions, since the other three
        // are fixed to play-pause-buffering, previous, next.

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // Although setting media session actions on older android versions doesn't seem to
            // cause any trouble, it also doesn't seem to do anything, so we don't do anything to
            // save battery. Check out NotificationUtil.updateActions() to see what happens on
            // older android versions.
            return;
        }

        if (!mediaSession.isActive()) {
            // mediaSession will be inactive after destroyPlayer is called
            return;
        }

        // only use the fourth and fifth actions (the settings page also shows only the last 2 on
        // Android 13+)
        final List<NotificationActionData> newNotificationActions = IntStream.of(3, 4)
                .map(i -> player.getPrefs().getInt(
                        player.getContext().getString(NotificationConstants.SLOT_PREF_KEYS[i]),
                        NotificationConstants.SLOT_DEFAULTS[i]))
                .mapToObj(action -> NotificationActionData
                        .fromNotificationActionEnum(player, action))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // avoid costly notification actions update, if nothing changed from last time
        if (!newNotificationActions.equals(prevNotificationActions)) {
            prevNotificationActions = newNotificationActions;
            sessionConnector.setCustomActionProviders(
                    newNotificationActions.stream()
                            .map(data -> new SessionConnectorActionProvider(data, context))
                            .toArray(SessionConnectorActionProvider[]::new));
        }
    }
