    public static void main(String[] args) throws InterruptedException {
        List<Pojo> list = new ArrayList<Pojo>();

        for (int i = 0; i < 40; i ++) {
            Pojo pojo = new Pojo();
            pojo.setName("name " + i);
            pojo.setAge(i + 2);

            list.add(pojo);
        }

        System.out.println(p);

        while (true) {
            int random = new Random().nextInt(40);
            String name = list.get(random).getName();
            list.get(random).setName(null);
            test(list);
            list.get(random).setName(name);
            Thread.sleep(1000L);
        }
    }
