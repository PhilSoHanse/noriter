package com.codewarts.noriter.auth.oauth.service;

import com.codewarts.noriter.auth.oauth.dto.OAuthAccessToken;
import com.codewarts.noriter.common.domain.Member;

public interface OAuthService {

    OAuthAccessToken reqeustAccessToken(String code);

    Member reqeustUserInfo(OAuthAccessToken oauthAccessToken);
}