package com.diancan.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diancan.admin.entity.UserEntity;
import com.diancan.admin.utils.JsonResult;
import com.diancan.admin.utils.Query;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.catalina.User;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

// 管理员
@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private Gson gson;

    @Autowired
    private HttpSession httpSession;

    private String setPassword(String password) {
        return encoder.encode(password);
    }

    private static final String collectionName = "admin";

    private static final String superAdminIdString = "794792045ea63d1b004a31be2ca86926";

    @GetMapping("/login")
    public String login()
    {
        return "/admin/user/login";
    }

    /**
     * 登录逻辑
     * @param jsonObject json对象
     * @return JsonResult
     * @throws WxErrorException  可能抛出WxSDK的异常
     */
    @PostMapping("/login")
    @ResponseBody
    public JsonResult login(@RequestBody JSONObject jsonObject) throws WxErrorException {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        if (username==null||username.length()<=0){
            return JsonResult.fail("用户名不能为空");
        }
        if (password==null||password.length()<=0){
            return JsonResult.fail("密码不能为空");
        }

        String user = Query.one(collectionName,"username",username);

        if (user==null||user.length()<=0){
            return JsonResult.fail("账号不存在!");
        }

        UserEntity userEntity = gson.fromJson(user, UserEntity.class);

        boolean isPasswordMatched = encoder.matches(password,userEntity.getPassword());

        if (isPasswordMatched){
            httpSession.setAttribute("user",userEntity);
            return JsonResult.success("登录成功!");
        }
        return JsonResult.fail("登录失败,请检查用户名或密码后重试!");

    }


    /**
     * 登出
     * @param response 跳转到登录页面
     * @throws IOException io异常
     */
    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {

        if (httpSession.getAttribute("user")!=null){
            httpSession.removeAttribute("user");
            response.sendRedirect("/admin/user/login");
        }

    }

    /**
     * 首页显示页面
     * @return 视图
     */
    @GetMapping("/index")
    public String index()
    {
        return "/admin/user/index";
    }

    /**
     * 添加用户页面
     * @return 视图
     */
    @GetMapping("/add")
    public String add()
    {
        return "/admin/user/add";
    }

    @PostMapping("/store")
    @ResponseBody
    public JsonResult store(@RequestBody JSONObject jsonObject) throws WxErrorException {
        String username = jsonObject.getString("username");
        String nickname = jsonObject.getString("nickname");
        String password = jsonObject.getString("password");

        if (username==null||username.length()<=0){
            return JsonResult.fail("用户名不能为空");
        }
        if (nickname==null||nickname.length()<=0){
            return JsonResult.fail("昵称不能为空");
        }
        if (password==null||password.length()<=0){
            return JsonResult.fail("密码不能为空");
        }
        Boolean exists = Query.exists(collectionName,"username",username);
        if (exists){
            return JsonResult.fail("该用户名 "+username+" 已存在");
        }

        Query.insertUser(collectionName,username,nickname,setPassword(password));

        return JsonResult.success("添加成功!");
    }

    /**
     * 获取数据
     * @return JsonResult
     * @throws WxErrorException 可能抛出WxSDK的异常
     */
    @GetMapping("/list")
    @ResponseBody
    public JsonResult list(@RequestParam(value = "content",required = false) String content ) throws WxErrorException {

        JSONArray jsonArray = null;

        if (content==null){
            content="";
        }

        if (content.isEmpty()){
            // 获取数据
            jsonArray = Query.list(collectionName);
        }else{
            jsonArray = Query.where(collectionName,"nickname",content);
        }

        // 返回数据
        return JsonResult.data(jsonArray);
    }

    /**
     * 删除数据
     * @param id id字符串
     * @return JsonResult
     * @throws WxErrorException 可能抛出WxSDK的异常
     */
    @DeleteMapping("/destory")
    @ResponseBody
    public JsonResult destory(@RequestParam("id") String id) throws WxErrorException {
        if (id.length()<=0){
            return JsonResult.fail("id不能为空");
        }

        if (id.equals(superAdminIdString)){
            return JsonResult.fail("禁止删除默认管理员");
        }

        Query.destory(collectionName,id);

        return JsonResult.success("删除成功");
    }

    /**
     * 更新数据
     * @param jsonObject json对象字符串
     * @return JsonResult
     * @throws WxErrorException 可能抛出WxSDK的异常
     */
    @PutMapping("/update")
    @ResponseBody
    public JsonResult update(@RequestBody JSONObject jsonObject) throws WxErrorException {
        String id = jsonObject.getString("id");
        String field = jsonObject.getString("field");
        String value = jsonObject.getString("value");

        if (id.length()<=0){
            return JsonResult.fail("id不能为空");
        }

        if (field.equals("password_empty")){
            field = "password";
            value = setPassword(value);
        }

        Object update = Query.update(collectionName,id,field,value);

        return JsonResult.success();
    }
}
