package com.diancan.admin.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.cloud.WxCloudDatabaseUpdateResult;
import com.alibaba.fastjson.JSONArray;
import com.diancan.admin.config.WxMaConfiguration;
import com.google.gson.JsonArray;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.Arrays;

/**
 * 增删改查工具
 */
public class Query {
    static WxMaService wxMaService = WxMaConfiguration.getMaService("wxd5b26a2ab40593a3");

    /**
     * 获取数据
     * @param collectionName 集合名称
     * @return JSONArray Json数组
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static JSONArray list(String collectionName) throws WxErrorException {
        String[] data = wxMaService.getCloudService().databaseQuery("x1y-p0g6f","db.collection(\""+collectionName+"\").get()").getData();

        return JSONArray.parseArray(Arrays.toString(data));
    }

    /**
     * 获取一条数据
     * @param collectionName 集合名称
     * @return JSONArray Json数组
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static JSONArray getById(String collectionName,String id) throws WxErrorException {
        String[] data = wxMaService.getCloudService().databaseQuery("x1y-p0g6f","db.collection(\""+collectionName+"\").where({_id:\""+id+"\"}).get()").getData();

        return JSONArray.parseArray(Arrays.toString(data));
    }

    /**
     * 模糊查询数据
     * @param collectionName 集合名称
     * @return JSONArray Json数组
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static JSONArray where(String collectionName,String field,String condition) throws WxErrorException {
        String[] data = wxMaService.getCloudService().databaseQuery("x1y-p0g6f","db.collection(\""+collectionName+"\").where({"+field+":{$regex:'.*"+condition+"',$options:'i'}}).get()").getData();

        return JSONArray.parseArray(Arrays.toString(data));
    }
    /**
     * 数据是否存在
     * @param collectionName 集合名称
     * @return Boolean boolean
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static Boolean exists(String collectionName, String field, String condition) throws WxErrorException {
        int data = wxMaService.getCloudService().databaseQuery("x1y-p0g6f","db.collection(\""+collectionName+"\").where({"+field+":\""+condition+"\"}).get()").getData().length;

        return data > 0;
    }

    /**
     * 查询一条数据
     * @param collectionName 集合名称
     * @return String 结果
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static String one(String collectionName, String field, String condition) throws WxErrorException {
        String[] data = wxMaService.getCloudService().databaseQuery("x1y-p0g6f","db.collection(\""+collectionName+"\").where({"+field+":\""+condition+"\"}).limit(1).get()").getData();

        return data[0];
    }

    /**
     * 删除数据
     * @param collectionName 集合名称
     * @param id id
     * @return affectedNums 受影响的行数
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static Integer destory(String collectionName,String id) throws WxErrorException {

        return wxMaService.getCloudService()
                .databaseDelete("x1y-p0g6f","db.collection(\""+collectionName+"\").where({_id:\""+id+"\"}).remove()");
    }

    /**
     * 更新一条数据
     * @param collectionName 集合名称
     * @return Integer 影响行数
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static Integer update(String collectionName, String id,String field,String value) throws WxErrorException {
        WxCloudDatabaseUpdateResult data = wxMaService
                .getCloudService()
                .databaseUpdate("x1y-p0g6f","db.collection(\""+collectionName+"\").where({_id:\""+id+"\"}).update({data:{"+field+":\""+value+"\"}})");
        return data.getModified().intValue();
    }

    /**
     * 新增一条数据
     * @param collectionName 集合名称
     * @return JsonArray Json数组
     * @throws WxErrorException 可能会抛出WxSDK的异常
     */
    public static JsonArray insertUser(String collectionName, String username, String nickname, String password) throws WxErrorException {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        return wxMaService
                .getCloudService()
                .databaseAdd("x1y-p0g6f","db.collection(\""+collectionName+"\").add({data:{username:\""+username+"\",nickname:\""+nickname+"\",password:\""+password+"\",create_time:\""+timestamp+"\"}})");
    }
}
