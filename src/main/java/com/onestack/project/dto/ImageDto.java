package com.onestack.project.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ImageDto {
    private String fileName;
    private String url;
    private long size;
    private Instant createdAt;
    private Instant modifiedAt;
}