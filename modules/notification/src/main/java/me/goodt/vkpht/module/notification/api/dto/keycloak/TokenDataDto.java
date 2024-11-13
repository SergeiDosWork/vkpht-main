package me.goodt.vkpht.module.notification.api.dto.keycloak;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TokenDataDto extends TokenDataDtoBase {

    private final static String EXPIRES_IN_TAG = "expires_in";
    private final static String REFRESH_EXPIRES_IN_TAG = "refresh_expires_in";
    private final static String REFRESH_TOKEN_TAG = "refresh_token";
    private final static String SESSION_STATE_TAG = "session_state";
    private Integer expiresIn;
    private Integer refreshExpiresIn;
    private String refreshToken;
    private String sessionState;
    private String scope;

    public TokenDataDto() {

    }

    public TokenDataDto(String accessToken, Integer expiresIn, Integer refreshExpiresIn,
                        String refreshToken, String tokenType, String sessionState, String scope) {
        super(tokenType, accessToken);
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.refreshToken = refreshToken;
        this.sessionState = sessionState;
        this.scope = scope;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    @JsonSetter(TokenDataDto.EXPIRES_IN_TAG)
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Integer getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    @JsonSetter(TokenDataDto.REFRESH_EXPIRES_IN_TAG)
    public void setRefreshExpiresIn(Integer refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonSetter(TokenDataDto.REFRESH_TOKEN_TAG)
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSessionState() {
        return sessionState;
    }

    @JsonSetter(TokenDataDto.SESSION_STATE_TAG)
    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
