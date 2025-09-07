    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getAuditData(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "principal", required = false) String principal,
            @RequestParam(value = "startDate", required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate endDate,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<PersistentAuditEvent> events;

        String mode;

        if (type != null && principal != null && startDate != null && endDate != null) {
            mode = "principal + type + startDate + endDate";
            Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            events =
                    auditRepository.findByPrincipalAndTypeAndTimestampBetween(
                            principal, type, start, end, pageable);
        } else if (type != null && principal != null) {
            mode = "principal + type";
            events = auditRepository.findByPrincipalAndType(principal, type, pageable);
        } else if (type != null && startDate != null && endDate != null) {
            mode = "type + startDate + endDate";
            Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            events = auditRepository.findByTypeAndTimestampBetween(type, start, end, pageable);
        } else if (principal != null && startDate != null && endDate != null) {
            mode = "principal + startDate + endDate";
            Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            events =
                    auditRepository.findByPrincipalAndTimestampBetween(
                            principal, start, end, pageable);
        } else if (startDate != null && endDate != null) {
            mode = "startDate + endDate";
            Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            events = auditRepository.findByTimestampBetween(start, end, pageable);
        } else if (type != null) {
            mode = "type";
            events = auditRepository.findByType(type, pageable);
        } else if (principal != null) {
            mode = "principal";
            events = auditRepository.findByPrincipal(principal, pageable);
        } else {
            mode = "all";
            events = auditRepository.findAll(pageable);
        }

        // Logging
        List<PersistentAuditEvent> content = events.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        response.put("totalPages", events.getTotalPages());
        response.put("totalElements", events.getTotalElements());
        response.put("currentPage", events.getNumber());

        return response;
    }
