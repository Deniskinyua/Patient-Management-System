package com.pms.authservice.utils;

import lombok.AllArgsConstructor;

import java.security.Key;

@AllArgsConstructor
public class JwtUtil{

    private final Key secretKey;


}
