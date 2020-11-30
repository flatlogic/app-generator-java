package com.flatlogic.app.generator.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class JwtUser {

    private UUID id;

    private String email;

}
