package me.minkyu.springbootdeveloper.config.oauth;

import me.minkyu.springbootdeveloper.config.jwt.TokenProvider;
import me.minkyu.springbootdeveloper.repository.RefreshTokenRepository;
import me.minkyu.springbootdeveloper.service.UserService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public OAuth2SuccessHandler(TokenProvider tokenProvider,
                                RefreshTokenRepository refreshTokenRepository,
                                OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository,
                                UserService userService) {



    }
}
