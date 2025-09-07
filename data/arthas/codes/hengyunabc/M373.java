	@ReadOperation
	public Map<String, Object> invoke() {
		Map<String, Object> result = new HashMap<String, Object>();

		if (arthasConfigMap != null) {
			result.put("arthasConfigMap", arthasConfigMap);
		}

		String errorMessage = arthasAgent.getErrorMessage();
		if (errorMessage != null) {
			result.put("errorMessage", errorMessage);
		}

		return result;
	}
