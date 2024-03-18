package com.sky.service.impl;

import com.alibaba.druid.support.json.JSONParser;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import com.sun.javafx.fxml.builder.URLBuilder;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private JwtProperties jwtProperties;

    private static final String WX_CODE2SESSION_ADDRESS = "https://api.weixin.qq.com/sns/jscode2session";


    @Override
    public UserLoginVO login(UserLoginDTO dto) {
        // 构造请求参数
        HashMap<String, String> params = new HashMap<>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", dto.getCode());
        params.put("grant_type", "authorization_code");
        // 请求登录，返回结果
        String responseJson = HttpClientUtil.doGet(WX_CODE2SESSION_ADDRESS, params);
        JSONParser jsonParser = new JSONParser(responseJson);
        Map<String, Object> response = jsonParser.parseMap();
        // 从返回结果中获得openid
        String openid = (String) response.get("openid");
        if (openid == null)
            throw new LoginFailedException("登录失败！");
        // 查询user表，该用户是否是第一次登录，若是，需要注册
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            user = User.builder().createTime(LocalDateTime.now()).openid(openid).build();
            userMapper.insert(user);
        }
        // 制作jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        // 返回VO
        return UserLoginVO.builder().id(user.getId()).openid(user.getOpenid()).token(token).build();
    }
}
