package com.diancan.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diancan.admin.utils.JsonResult;
import com.diancan.admin.utils.Query;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 校园兼职
@Controller
@RequestMapping("/admin/parttime_job")
public class ParttimeJobController {

    private static final String collectionName = "jianzhi";

    /**
     * 首页显示页面
     * @return
     */
    @GetMapping("/index")
    public String index()
    {
        return "/admin/parttime_job/index";
    }

    /**
     * 获取校园兼职数据
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
            jsonArray = Query.where(collectionName,"des",content);
        }

        // 返回数据
        return JsonResult.data(jsonArray);
    }

    @DeleteMapping("/destory")
    @ResponseBody
    public JsonResult destory(@RequestParam("id") String id) throws WxErrorException {
        if (id.length()<=0){
            return JsonResult.fail("id不能为空");
        }

        Query.destory(collectionName,id);

        return JsonResult.success("删除成功");
    }

    @PutMapping("/update")
    @ResponseBody
    public JsonResult update(@RequestBody JSONObject jsonObject) throws WxErrorException {
        String id = jsonObject.getString("id");
        String field = jsonObject.getString("field");
        String value = jsonObject.getString("value");

        if (id.length()<=0){
            return JsonResult.fail("id不能为空");
        }

        Object update = Query.update(collectionName,id,field,value);

        return JsonResult.success();
    }
}
