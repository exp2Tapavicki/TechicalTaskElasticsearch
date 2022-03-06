package com.task.elasticsearch.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportFile {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("content")
    private String content;

    @JsonProperty("fileType")
    private FileType fileType;

    public static Map<String, Object> getAsMap(final ObjectMapper OBJECT_MAPPER, final ImportFile importFile) {
        return OBJECT_MAPPER.convertValue(importFile, new TypeReference<Map<String, Object>>() {
        });
    }

}
