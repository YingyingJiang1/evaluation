    private void addDefaultConverter() {
        converters.put(new ConvertiblePair(String.class, Integer.class), new StringToIntegerConverter());
        converters.put(new ConvertiblePair(String.class, Long.class), new StringToLongConverter());

        converters.put(new ConvertiblePair(String.class, Boolean.class), new StringToBooleanConverter());

        converters.put(new ConvertiblePair(String.class, InetAddress.class), new StringToInetAddressConverter());

        converters.put(new ConvertiblePair(String.class, Enum.class), new StringToEnumConverter());

        converters.put(new ConvertiblePair(String.class, Arrays.class), new StringToArrayConverter(this));

    }
