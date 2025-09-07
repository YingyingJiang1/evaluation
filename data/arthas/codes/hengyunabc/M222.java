    @RequestMapping(value = "/api/stat")
    @ResponseBody
    public Map<String, Object> execute(@RequestParam(value = "ip", required = true) String ip,
            @RequestParam(value = "version", required = true) String version,
            @RequestParam(value = "agentId", required = false) String agentId,
            @RequestParam(value = "command", required = true) String command,
            @RequestParam(value = "arguments", required = false, defaultValue = "") String arguments) {

        logger.info("arthas stat, ip: {}, version: {}, agentId: {}, command: {}, arguments: {}", ip, version, agentId, command, arguments);

        Map<String, Object> result = new HashMap<>();

        result.put("success", true);

        return result;
    }
