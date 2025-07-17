package com.shmashine.socket.netty.server.ssl;

import java.io.File;
import java.util.Objects;

import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * ServerSSLFactory
 *
 * @author chenx
 */
@Slf4j
public class ServerSSLFactory {

    private static SslContext SERVER_CONTEXT;

    public static SslContext sslContext() {
        //引入SSL安全验证
        //certChainFile 为服务端证书
        //keyFile为服务端密钥
        //rootFile为CA根证书
        //linux 文件路径
        if (SERVER_CONTEXT != null) {
            return SERVER_CONTEXT;
        }
        //linux 文件路径
        /*String certChainFilePath = "/ssl-tls/tcp/server-cert.pem";
        String keyFilePath = "/ssl-tls/tcp/server-pkcs8.pem";
        String rootFilePath = "/ssl-tls/tcp/ca-cert.pem";

        File certChainFile = new File(certChainFilePath);
        File keyFile = new File(keyFilePath);
        File rootFile = new File(rootFilePath);*/

        var classLoader = ServerSSLFactory.class.getClassLoader();
        try {
            var rootFile = new File(Objects.requireNonNull(classLoader
                    .getResource("/certificate_server/ca-cert.pem")).toURI());

            var keyFile = new File(Objects.requireNonNull(classLoader
                    .getResource("/certificate_server/server-pkcs8.pem")).toURI());

            var certChainFile = new File(Objects.requireNonNull(classLoader
                    .getResource("/certificate_server/server-cert.pem")).toURI());

            SERVER_CONTEXT = SslContextBuilder.forServer(certChainFile, keyFile).trustManager(rootFile)
                    .clientAuth(ClientAuth.REQUIRE).build();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return SERVER_CONTEXT;
    }

}
