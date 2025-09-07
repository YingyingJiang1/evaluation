    public static ComplexObject createComplexObject() {
        // 创建一个 ComplexObject 对象
        ComplexObject complexObject = new ComplexObject();

        // 设置基本类型的值
        complexObject.setId(1);
        complexObject.setName("Complex Object");
        complexObject.setValue(3.14);

        // 设置基本类型的数组
        int[] numbers = { 1, 2, 3, 4, 5 };
        complexObject.setNumbers(numbers);

        Long[] longNumbers = {10086l,10087l,10088l,10089l,10090l,10091l};
        complexObject.setLongNumbers(longNumbers);

        // 创建并设置嵌套对象
        ComplexObject.NestedObject nestedObject = new ComplexObject.NestedObject();
        nestedObject.setNestedId(10);
        nestedObject.setNestedName("Nested Object");
        nestedObject.setFlag(true);
        complexObject.setNestedObject(nestedObject);


        List<String> stringList = new ArrayList<>();
        stringList.add("foo");
        stringList.add("bar");
        stringList.add("baz");
        complexObject.setStringList(stringList);

        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("one", 1);
        stringIntegerMap.put("two", 2);
        complexObject.setStringIntegerMap(stringIntegerMap);

        complexObject.setDoubleArray(new Double[] { 1.0, 2.0, 3.0 });

        complexObject.setComplexArray(null);

        complexObject.setCollection(Arrays.asList("element1", "element2"));


        // 创建并设置复杂对象数组
        ComplexObject[] complexArray = new ComplexObject[2];

        ComplexObject complexObject1 = new ComplexObject();
        complexObject1.setId(2);
        complexObject1.setName("Complex Object 1");
        complexObject1.setValue(2.71);

        ComplexObject complexObject2 = new ComplexObject();
        complexObject2.setId(3);
        complexObject2.setName("Complex Object 2");
        complexObject2.setValue(1.618);

        complexArray[0] = complexObject1;
        complexArray[1] = complexObject2;

        complexObject.setComplexArray(complexArray);

        // 创建并设置多维数组
        int[][] multiDimensionalArray = { { 1, 2, 3 }, { 4, 5, 6 } };
        complexObject.setMultiDimensionalArray(multiDimensionalArray);

        // 设置数组中的基本元素数组
        String[] stringArray = { "Hello", "World" };
        complexObject.setStringArray(stringArray);

        // 输出 ComplexObject 对象的信息
        System.out.println(complexObject);

        return complexObject;
    }
