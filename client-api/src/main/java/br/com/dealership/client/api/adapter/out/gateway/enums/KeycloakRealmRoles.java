package br.com.dealership.client.api.adapter.out.gateway.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KeycloakRealmRoles {
    CLIENT("CLIENT");

    private final String roleName;
}