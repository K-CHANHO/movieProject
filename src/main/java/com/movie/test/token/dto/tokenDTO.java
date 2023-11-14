package com.movie.test.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class tokenDTO {

    private String uid;
    private String type;
    private String jwt;
}
