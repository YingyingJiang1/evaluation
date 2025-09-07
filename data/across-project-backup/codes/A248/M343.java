    private static EmailContent extractEmailContentAdvanced(
            byte[] emlBytes, EmlToPdfRequest request) {
        try {
            // Use Jakarta Mail for processing
            Class<?> sessionClass = Class.forName("jakarta.mail.Session");
            Class<?> mimeMessageClass = Class.forName("jakarta.mail.internet.MimeMessage");

            Method getDefaultInstance =
                    sessionClass.getMethod("getDefaultInstance", Properties.class);
            Object session = getDefaultInstance.invoke(null, new Properties());

            // Cast the session object to the proper type for the constructor
            Class<?>[] constructorArgs = new Class<?>[] {sessionClass, InputStream.class};
            Constructor<?> mimeMessageConstructor =
                    mimeMessageClass.getConstructor(constructorArgs);
            Object message =
                    mimeMessageConstructor.newInstance(session, new ByteArrayInputStream(emlBytes));

            return extractEmailContentAdvanced(message, request);

        } catch (ReflectiveOperationException e) {
            // Create basic EmailContent from basic processing
            EmailContent content = new EmailContent();
            content.setHtmlBody(convertEmlToHtmlBasic(emlBytes, request));
            return content;
        }
    }
