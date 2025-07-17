package com.shmashine.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.shmashine.auth.AuthRedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class ServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private Environment env;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 配置token获取和验证时的策略
        security
                // url:/oauth/token_key
                // exposes public key for token verification if using JWT tokens
                .tokenKeyAccess("permitAll()")
                // url:/oauth/check_token allow check token
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 1. 授权的客户端配置。客户端配置在DB中，通过jdbc进行访问。<br>
     * <p>
     * 2. secret密码配置从 Spring Security 5.0开始必须以 {加密方式}+加密后的密码 这种格式填写<br>
     * 当前版本5新增支持加密方式： <br>
     * (1) bcrypt - BCryptPasswordEncoder (Also used for encoding) <br>
     * (2)ldap - LdapShaPasswordEncoder <br>
     * (3)MD4 - Md4PasswordEncoder<br>
     * (4) MD5 - new MessageDigestPasswordEncoder("MD5") <br>
     * (5)noop - NoOpPasswordEncoder <br>
     * (6)pbkdf2 - Pbkdf2PasswordEncoder <br>
     * (7)scrypt - SCryptPasswordEncoder <br>
     * (8)SHA-1 - new MessageDigestPasswordEncoder("SHA-1") <br>
     * (9) SHA-256 - new MessageDigestPasswordEncoder("SHA-256")<br>
     * (10) sha256 - StandardPasswordEncoder
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 数据记录在oauth_client_details表中（注意加密方式，参考上面的说明）
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                // 配置Redis参数和tokenStore
                .tokenServices(tokenServices())
                // 如果不添加userDetailsService，刷新access_token时会报错
                .userDetailsService(userDetailsService);
    }

    @Primary
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        // 从配置文件(application.properties)中读取token超时时间
        int tokenSeconds = env.getProperty("biz.token.valid.seconds", Integer.class);
        int refreshTokenSeconds = env.getProperty("biz.refreshtoken.valid.seconds", Integer.class);

        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setAccessTokenValiditySeconds(tokenSeconds);
        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenSeconds);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setTokenStore(new AuthRedisTokenStore(redisConnectionFactory));
        return defaultTokenServices;
    }
}
