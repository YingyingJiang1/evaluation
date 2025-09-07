    private static void initDefaultWriteConverter() {
        defaultWriteConverter = MapUtils.newHashMapWithExpectedSize(40);
        putWriteConverter(new BigDecimalNumberConverter());
        putWriteConverter(new BigIntegerNumberConverter());
        putWriteConverter(new BooleanBooleanConverter());
        putWriteConverter(new ByteNumberConverter());
        putWriteConverter(new DateDateConverter());
        putWriteConverter(new LocalDateTimeDateConverter());
        putWriteConverter(new LocalDateDateConverter());
        putWriteConverter(new DoubleNumberConverter());
        putWriteConverter(new FloatNumberConverter());
        putWriteConverter(new IntegerNumberConverter());
        putWriteConverter(new LongNumberConverter());
        putWriteConverter(new ShortNumberConverter());
        putWriteConverter(new StringStringConverter());
        putWriteConverter(new FileImageConverter());
        putWriteConverter(new InputStreamImageConverter());
        putWriteConverter(new ByteArrayImageConverter());
        putWriteConverter(new BoxingByteArrayImageConverter());
        putWriteConverter(new UrlImageConverter());

        // In some cases, it must be converted to string
        putWriteStringConverter(new BigDecimalStringConverter());
        putWriteStringConverter(new BigIntegerStringConverter());
        putWriteStringConverter(new BooleanStringConverter());
        putWriteStringConverter(new ByteStringConverter());
        putWriteStringConverter(new DateStringConverter());
        putWriteStringConverter(new LocalDateStringConverter());
        putWriteStringConverter(new LocalDateTimeStringConverter());
        putWriteStringConverter(new DoubleStringConverter());
        putWriteStringConverter(new FloatStringConverter());
        putWriteStringConverter(new IntegerStringConverter());
        putWriteStringConverter(new LongStringConverter());
        putWriteStringConverter(new ShortStringConverter());
        putWriteStringConverter(new StringStringConverter());
    }
