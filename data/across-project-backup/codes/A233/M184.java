    private Map<String, Object> getDockerMetrics() {
        Map<String, Object> dockerMetrics = new HashMap<>();

        // Network-related Docker info
        dockerMetrics.put("docker_network_mode", System.getenv("DOCKER_NETWORK_MODE"));

        // Container name (if set)
        String containerName = System.getenv("CONTAINER_NAME");
        if (containerName != null && !containerName.isEmpty()) {
            dockerMetrics.put("container_name", containerName);
        }

        // Docker compose information
        String composeProject = System.getenv("COMPOSE_PROJECT_NAME");
        String composeService = System.getenv("COMPOSE_SERVICE_NAME");
        if (composeProject != null && composeService != null) {
            dockerMetrics.put("compose_project", composeProject);
            dockerMetrics.put("compose_service", composeService);
        }

        // Kubernetes-specific info (if running in K8s)
        String k8sPodName = System.getenv("KUBERNETES_POD_NAME");
        if (k8sPodName != null) {
            dockerMetrics.put("k8s_pod_name", k8sPodName);
            dockerMetrics.put("k8s_namespace", System.getenv("KUBERNETES_NAMESPACE"));
            dockerMetrics.put("k8s_node_name", System.getenv("KUBERNETES_NODE_NAME"));
        }

        // New environment variables
        dockerMetrics.put("version_tag", System.getenv("VERSION_TAG"));
        dockerMetrics.put("additional_features_off", System.getenv("ADDITIONAL_FEATURES_OFF"));
        dockerMetrics.put("fat_docker", System.getenv("FAT_DOCKER"));

        return dockerMetrics;
    }
