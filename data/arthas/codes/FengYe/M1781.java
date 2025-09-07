    public static byte[] getBytes(ByteBuf buf) {
        if (buf.hasArray()) {
            // 如果 ByteBuf 是一个支持底层数组的实现，直接获取数组
            return buf.array();
        } else {
            // 创建一个新的 byte 数组
            byte[] bytes = new byte[buf.readableBytes()];
            // 将 ByteBuf 的内容复制到 byte 数组中
            buf.getBytes(buf.readerIndex(), bytes);
            return bytes;
        }
    }
