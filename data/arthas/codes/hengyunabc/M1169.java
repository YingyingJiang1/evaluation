    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String name = method.getName();

        if (name.equals("apply")) {
            Session session = termImpl.getSession();
            if (session != null) {
                boolean authenticated = session.get(ArthasConstants.SUBJECT_KEY) != null;
                if (authenticated) {
                    return method.invoke(target, args);
                } else {
                    Readline.Interaction interaction = (Interaction) args[0];
                    // 必要
                    interaction.resume();
                    return null;
                }
            }
        }

        return method.invoke(target, args);
    }
