//package com.shmashine.common.utils;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Value;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.auth0.jwt.interfaces.JWTVerifier;
//import com.shmashine.common.constants.SecurityContants;
//
//
/// **
// * 生成JWT 校验JWT 管理JWT
// *
// * @author LLF
// * @since 2020/4/14 10:15
// */
//
//public class SecurityUtils {
//
//    @Value("${spring.application.name}")
//    private static String CLIENTNAME;
//
//    /**
//     * 生成 Token
//     *
//     * @return token
//     */
//    public static String createJwtToekn(String username) {
//        // 创建Token过程
//        String token = JWT.create()
//                .withAudience(username)
//                .withJWTId(UUID.randomUUID().toString())
//                .withSubject(username)
//                .withIssuer(CLIENTNAME)
//                .withClaim(username, 123)
//                .withArrayClaim("clami", new Long[]{123L, 456L, 789L})
//                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityContants.EXPIRESAT)) //定义token的有效期
//                .sign(Algorithm.HMAC256(SecurityContants.SIGN)); // 加密秘钥，也可以使用用户保持在数据库中的密码字符串
//        return token;
//    }
//
//    /**
//     * 验证Token
//     *
//     * @param token token
//     * @return 是否有效
//     */
//    public static Boolean verifyJwtToken(String token) {
//        try {
//            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SecurityContants.SIGN)).build();
//            jwtVerifier.verify(token);
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 添加无需认证的接口
//     *
//     * @param token token
//     * @return 结果
//     */
//    public static List<String> getAudience(String token) {
//        try {
//            DecodedJWT decode = JWT.decode(token);
//            List<String> audience = decode.getAudience();
//            return audience;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}
