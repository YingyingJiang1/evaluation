    @GetMapping("/pipeline")
    @Hidden
    public String pipelineForm(Model model) {
        model.addAttribute("currentPage", "pipeline");
        List<String> pipelineConfigs = new ArrayList<>();
        List<Map<String, String>> pipelineConfigsWithNames = new ArrayList<>();
        if (new File(runtimePathConfig.getPipelineDefaultWebUiConfigs()).exists()) {
            try (Stream<Path> paths =
                    Files.walk(Paths.get(runtimePathConfig.getPipelineDefaultWebUiConfigs()))) {
                List<Path> jsonFiles =
                        paths.filter(Files::isRegularFile)
                                .filter(p -> p.toString().endsWith(".json"))
                                .toList();
                for (Path jsonFile : jsonFiles) {
                    String content = Files.readString(jsonFile, StandardCharsets.UTF_8);
                    pipelineConfigs.add(content);
                }
                for (String config : pipelineConfigs) {
                    Map<String, Object> jsonContent =
                            new ObjectMapper()
                                    .readValue(config, new TypeReference<Map<String, Object>>() {});
                    String name = (String) jsonContent.get("name");
                    if (name == null || name.length() < 1) {
                        String filename =
                                jsonFiles
                                        .get(pipelineConfigs.indexOf(config))
                                        .getFileName()
                                        .toString();
                        name = filename.substring(0, filename.lastIndexOf('.'));
                    }
                    Map<String, String> configWithName = new HashMap<>();
                    configWithName.put("json", config);
                    configWithName.put("name", name);
                    pipelineConfigsWithNames.add(configWithName);
                }
            } catch (IOException e) {
                log.error("exception", e);
            }
        }
        if (pipelineConfigsWithNames.size() == 0) {
            Map<String, String> configWithName = new HashMap<>();
            configWithName.put("json", "");
            configWithName.put("name", "No preloaded configs found");
            pipelineConfigsWithNames.add(configWithName);
        }
        model.addAttribute("pipelineConfigsWithNames", pipelineConfigsWithNames);
        model.addAttribute("pipelineConfigs", pipelineConfigs);
        return "pipeline";
    }
