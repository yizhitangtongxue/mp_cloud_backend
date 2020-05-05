package com.diancan.admin.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回json数据
 * @param <T>
 */
@SuppressWarnings("ALL")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonResult<T> {

    private Integer code;
    private String msg;
    private T data;

    public JsonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    public static JsonResult success()
    {
        return new JsonResult(0,"success");
    }

    public static JsonResult success(String msg)
    {
        return new JsonResult(0,msg);
    }

    public static JsonResult fail()
    {
        return new JsonResult(-1,"fail");
    }
    public static JsonResult fail(String msg)
    {
        return new JsonResult(-1,msg);
    }

    public static JsonResult data(Object data)
    {
        return new JsonResult(data);
    }
}
