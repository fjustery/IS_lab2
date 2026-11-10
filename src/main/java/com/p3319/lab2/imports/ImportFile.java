package com.p3319.lab2.imports;

import java.util.List;

import tools.jackson.databind.JsonNode;

public record ImportFile(ImportResourceType resourceType, List<JsonNode> items) {
}
