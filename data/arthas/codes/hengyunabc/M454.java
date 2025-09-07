    private void renderObject(Object obj, int deep, int expand, final StringBuilder buf) throws ObjectTooLargeException {

        if (null == obj) {
            appendStringBuilder(buf,"null");
        } else {

            final Class<?> clazz = obj.getClass();
            final String className = clazz.getSimpleName();

            // 7种基础类型,直接输出@类型[值]
            if (Integer.class.isInstance(obj)
                || Long.class.isInstance(obj)
                || Float.class.isInstance(obj)
                || Double.class.isInstance(obj)
                    //                    || Character.class.isInstance(obj)
                || Short.class.isInstance(obj)
                || Byte.class.isInstance(obj)
                || Boolean.class.isInstance(obj)) {
                appendStringBuilder(buf, format("@%s[%s]", className, obj));
            }

            // Char要特殊处理,因为有不可见字符的因素
            else if (Character.class.isInstance(obj)) {

                final Character c = (Character) obj;

                // ASCII的可见字符
                if (c >= 32
                    && c <= 126) {
                    appendStringBuilder(buf, format("@%s[%s]", className, c));
                }

                // ASCII的控制字符
                else if (ASCII_MAP.containsKey((byte) c.charValue())) {
                    appendStringBuilder(buf, format("@%s[%s]", className, ASCII_MAP.get((byte) c.charValue())));
                }

                // 超过ASCII的编码范围
                else {
                    appendStringBuilder(buf, format("@%s[%s]", className, c));
                }

            }

            // 字符串类型单独处理
            else if (String.class.isInstance(obj)) {
                appendStringBuilder(buf, "@");
                appendStringBuilder(buf, className);
                appendStringBuilder(buf, "[");
                for (Character c : ((String) obj).toCharArray()) {
                    switch (c) {
                        case '\n':
                            appendStringBuilder(buf, "\\n");
                            break;
                        case '\r':
                            appendStringBuilder(buf, "\\r");
                            break;
                        default:
                            appendStringBuilder(buf, c.toString());
                    }//switch
                }//for
                appendStringBuilder(buf, "]");
            }

            // 集合类输出
            else if (Collection.class.isInstance(obj)) {

                @SuppressWarnings("unchecked") final Collection<Object> collection = (Collection<Object>) obj;

                // 非根节点或空集合只展示摘要信息
                if (!isExpand(deep, expand)
                    || collection.isEmpty()) {

                    appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                      className,
                                      collection.isEmpty(),
                                      collection.size()));
                }

                // 展开展示
                else {
                    appendStringBuilder(buf, format("@%s[", className));
                    for (Object e : collection) {
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep+1; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        renderObject(e, deep + 1, expand, buf);
                        appendStringBuilder(buf, ",");
                    }
                    appendStringBuilder(buf, "\n");
                    for (int i = 0; i < deep; i++) {
                        appendStringBuilder(buf, TAB);
                    }
                    appendStringBuilder(buf, "]");
                }

            }


            // Map类输出
            else if (Map.class.isInstance(obj)) {
                @SuppressWarnings("unchecked") final Map<Object, Object> map = (Map<Object, Object>) obj;

                // 非根节点或空集合只展示摘要信息
                if (!isExpand(deep, expand)
                    || map.isEmpty()) {

                    appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                      className,
                                      map.isEmpty(),
                                      map.size()));

                } else {
                    appendStringBuilder(buf, format("@%s[", className));
                    for (Map.Entry<Object, Object> entry : map.entrySet()) {
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep+1; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        renderObject(entry.getKey(), deep + 1, expand, buf);
                        appendStringBuilder(buf, ":");
                        renderObject(entry.getValue(), deep + 1, expand, buf);
                        appendStringBuilder(buf, ",");
                    }
                    appendStringBuilder(buf, "\n");
                    for (int i = 0; i < deep; i++) {
                        appendStringBuilder(buf, TAB);
                    }
                    appendStringBuilder(buf, "]");
                }
            }


            // 数组类输出
            else if (obj.getClass().isArray()) {


                final String typeName = obj.getClass().getSimpleName();

                // int[]
                if (typeName.equals("int[]")) {

                    final int[] arrays = (int[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (int e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // long[]
                else if (typeName.equals("long[]")) {

                    final long[] arrays = (long[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (long e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // short[]
                else if (typeName.equals("short[]")) {

                    final short[] arrays = (short[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (short e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // float[]
                else if (typeName.equals("float[]")) {

                    final float[] arrays = (float[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (float e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // double[]
                else if (typeName.equals("double[]")) {

                    final double[] arrays = (double[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (double e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // boolean[]
                else if (typeName.equals("boolean[]")) {

                    final boolean[] arrays = (boolean[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (boolean e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // char[]
                else if (typeName.equals("char[]")) {

                    final char[] arrays = (char[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (char e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // byte[]
                else if (typeName.equals("byte[]")) {

                    final byte[] arrays = (byte[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (byte e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }

                }

                // Object[]
                else {
                    final Object[] arrays = (Object[]) obj;
                    // 非根节点或空集合只展示摘要信息
                    if (!isExpand(deep, expand)
                        || arrays.length == 0) {

                        appendStringBuilder(buf, format("@%s[isEmpty=%s;size=%d]",
                                          typeName,
                                          arrays.length == 0,
                                          arrays.length));

                    }

                    // 展开展示
                    else {
                        appendStringBuilder(buf, format("@%s[", className));
                        for (Object e : arrays) {
                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            renderObject(e, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");
                        }
                        appendStringBuilder(buf, "\n");
                        for (int i = 0; i < deep; i++) {
                            appendStringBuilder(buf, TAB);
                        }
                        appendStringBuilder(buf, "]");
                    }
                }

            }


            // Throwable输出
            else if (Throwable.class.isInstance(obj)) {

                if (!isExpand(deep, expand)) {
                    appendStringBuilder(buf, format("@%s[%s]", className, obj));
                } else {

                    final Throwable throwable = (Throwable) obj;
                    final StringWriter sw = new StringWriter();
                    final PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    appendStringBuilder(buf, sw.toString());
                }

            }

            // Date输出
            else if (Date.class.isInstance(obj)) {
                appendStringBuilder(buf, format("@%s[%s]", className, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(obj)));
            }

            else if (object instanceof Enum<?>) {
                appendStringBuilder(buf, format("@%s[%s]", className, obj));
            }

            // 普通Object输出
            else {

                if (!isExpand(deep, expand)) {
                    appendStringBuilder(buf, format("@%s[%s]", className, obj));
                } else {
                    appendStringBuilder(buf, format("@%s[", className));
                    final List<Field> fields;
                    Class<?> objClass = obj.getClass();
                    if (GlobalOptions.printParentFields) {
                        fields = new ArrayList<Field>();
                        // 当父类为null的时候说明到达了最上层的父类(Object类).
                        while (objClass != null) {
                            fields.addAll(Arrays.asList(objClass.getDeclaredFields()));
                            objClass = objClass.getSuperclass();
                        }
                    } else {
                        fields = new ArrayList<Field>(Arrays.asList(objClass.getDeclaredFields()));
                    }

                    for (Field field : fields) {

                        field.setAccessible(true);

                        try {

                            final Object value = field.get(obj);

                            appendStringBuilder(buf, "\n");
                            for (int i = 0; i < deep+1; i++) {
                                appendStringBuilder(buf, TAB);
                            }
                            appendStringBuilder(buf, field.getName());
                            appendStringBuilder(buf, "=");
                            renderObject(value, deep + 1, expand, buf);
                            appendStringBuilder(buf, ",");

                        } catch (ObjectTooLargeException t) {
                            buf.append("...");
                            break;
                        } catch (Throwable t) {
                            // ignore
                        }
                    }//for
                    appendStringBuilder(buf, "\n");
                    for (int i = 0; i < deep; i++) {
                        appendStringBuilder(buf, TAB);
                    }
                    appendStringBuilder(buf, "]");
                }

            }
        }
    }
