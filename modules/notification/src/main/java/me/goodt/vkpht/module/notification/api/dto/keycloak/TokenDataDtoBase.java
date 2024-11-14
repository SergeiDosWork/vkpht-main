package me.goodt.vkpht.module.notification.api.dto.keycloak;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TokenDataDtoBase {

    private final static String ACCESS_TOKEN_TAG = "access_token";
    private final static String TOKEN_TYPE_TAG = "token_type";
    private String accessToken;
    private String tokenType;

    public TokenDataDtoBase() {

    }

    public TokenDataDtoBase(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @JsonSetter(TokenDataDtoBase.ACCESS_TOKEN_TAG)
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    @JsonSetter(TokenDataDtoBase.TOKEN_TYPE_TAG)
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
