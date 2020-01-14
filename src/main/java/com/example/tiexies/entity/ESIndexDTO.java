package com.example.tiexies.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class ESIndexDTO {
    @NotEmpty
    @Pattern(regexp = "^[a-z][a-z0-9_-]*$")
    private String indexName;

    @Range(min = 1,max = 8)
    private int numShards;
}
