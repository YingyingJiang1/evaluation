                @Override
                protected String getSystemAttribute(String attributeName) {
                    try {
                        return System.getProperty(attributeName);
                    } catch (AccessControlException ex) {
                        return null;
                    }
                }
