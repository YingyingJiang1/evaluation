    private void tryCopyProperty(String key, Object value, boolean recheck)
            throws CheckstyleException {
        final BeanUtilsBean beanUtils = createBeanUtilsBean();

        try {
            if (recheck) {
                // BeanUtilsBean.copyProperties silently ignores missing setters
                // for key, so we have to go through great lengths here to
                // figure out if the bean property really exists.
                final PropertyDescriptor descriptor =
                        PropertyUtils.getPropertyDescriptor(this, key);
                if (descriptor == null) {
                    final String message = String.format(Locale.ROOT, "Property '%s' "
                            + "does not exist, please check the documentation", key);
                    throw new CheckstyleException(message);
                }
            }
            // finally we can set the bean property
            beanUtils.copyProperty(this, key, value);
        }
        catch (final InvocationTargetException | IllegalAccessException
                | NoSuchMethodException exc) {
            // There is no way to catch IllegalAccessException | NoSuchMethodException
            // as we do PropertyUtils.getPropertyDescriptor before beanUtils.copyProperty,
            // so we have to join these exceptions with InvocationTargetException
            // to satisfy UTs coverage
            final String message = String.format(Locale.ROOT,
                    "Cannot set property '%s' to '%s'", key, value);
            throw new CheckstyleException(message, exc);
        }
        catch (final IllegalArgumentException | ConversionException exc) {
            final String message = String.format(Locale.ROOT, "illegal value '%s' for property "
                    + "'%s'", value, key);
            throw new CheckstyleException(message, exc);
        }
    }
