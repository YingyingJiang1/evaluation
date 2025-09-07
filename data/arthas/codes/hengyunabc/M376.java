	@ConfigurationProperties(prefix = "arthas")
	@ConditionalOnMissingBean(name="arthasConfigMap")
	@Bean
	public HashMap<String, String> arthasConfigMap() {
		return new HashMap<String, String>();
	}
