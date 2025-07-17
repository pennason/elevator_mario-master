package com.shmashine.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;

import com.shmashine.auth.AuthUserDetail;
import com.shmashine.entity.LoginInfo;
import com.shmashine.mapper.TblSysUserMapper;
import com.shmashine.model.TblSysUser;
import com.shmashine.service.UserService;
import com.shmashine.util.AddressUtils;

@Primary
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TblSysUserMapper tblSysUserMapper;

    @Override
    public AuthUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        TblSysUser user = tblSysUserMapper.selectByPrimaryKey(username);
        if (user == null) {
            throw new UsernameNotFoundException("can not find user by " + username);
        }
        TblSysUser tblSysUser = new TblSysUser();
        tblSysUser.setV_user_id(user.getV_user_id());
        tblSysUser.setV_password(user.getV_password());
        return UserDetailConverter.convert(tblSysUser);
    }

    @Override
    public void getAddr(HttpServletRequest request) {

        String username = request.getParameter("username");

        String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }

        //获取ip详细信息
        JSONObject data = null;
        try {
            data = AddressUtils.getAddresses(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(IdUtil.createSnowflake(3, 5).nextIdStr());
        loginInfo.setUsername(username);
        loginInfo.setIp(ip);
        loginInfo.setLoginTime(new Date());

        if (data != null) {
            //国家
            loginInfo.setCountry(data.getStr("country"));
            //省份
            loginInfo.setRegion(data.getStr("region"));
            //城市
            loginInfo.setCity(data.getStr("city"));
            //区/县
            loginInfo.setCounty(data.getStr("county"));
            //互联网服务提供商
            loginInfo.setIsp(data.getStr("isp"));
            //ipv6
            loginInfo.setIpv6(data.getStr("ipv6"));
        }

        //登录信息落表
        tblSysUserMapper.insertLoginInfo(loginInfo);
    }

    private static class UserDetailConverter {
        static AuthUserDetail convert(TblSysUser user) {
            return new AuthUserDetail(user);
        }
    }
}
