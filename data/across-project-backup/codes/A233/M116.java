    private void migrateEnterpriseEditionToPremium(YamlHelper yaml, YamlHelper template) {
        if (yaml.getValueByExactKeyPath("enterpriseEdition", "enabled") != null) {
            template.updateValue(
                    List.of("premium", "enabled"),
                    yaml.getValueByExactKeyPath("enterpriseEdition", "enabled"));
        }
        if (yaml.getValueByExactKeyPath("enterpriseEdition", "key") != null) {
            template.updateValue(
                    List.of("premium", "key"),
                    yaml.getValueByExactKeyPath("enterpriseEdition", "key"));
        }
        if (yaml.getValueByExactKeyPath("enterpriseEdition", "SSOAutoLogin") != null) {
            template.updateValue(
                    List.of("premium", "proFeatures", "SSOAutoLogin"),
                    yaml.getValueByExactKeyPath("enterpriseEdition", "SSOAutoLogin"));
        }
        if (yaml.getValueByExactKeyPath("enterpriseEdition", "CustomMetadata", "autoUpdateMetadata")
                != null) {
            template.updateValue(
                    List.of("premium", "proFeatures", "CustomMetadata", "autoUpdateMetadata"),
                    yaml.getValueByExactKeyPath(
                            "enterpriseEdition", "CustomMetadata", "autoUpdateMetadata"));
        }
        if (yaml.getValueByExactKeyPath("enterpriseEdition", "CustomMetadata", "author") != null) {
            template.updateValue(
                    List.of("premium", "proFeatures", "CustomMetadata", "author"),
                    yaml.getValueByExactKeyPath("enterpriseEdition", "CustomMetadata", "author"));
        }
        if (yaml.getValueByExactKeyPath("enterpriseEdition", "CustomMetadata", "creator") != null) {
            template.updateValue(
                    List.of("premium", "proFeatures", "CustomMetadata", "creator"),
                    yaml.getValueByExactKeyPath("enterpriseEdition", "CustomMetadata", "creator"));
        }
        if (yaml.getValueByExactKeyPath("enterpriseEdition", "CustomMetadata", "producer")
                != null) {
            template.updateValue(
                    List.of("premium", "proFeatures", "CustomMetadata", "producer"),
                    yaml.getValueByExactKeyPath("enterpriseEdition", "CustomMetadata", "producer"));
        }
    }
