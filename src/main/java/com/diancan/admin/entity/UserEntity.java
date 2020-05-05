package com.diancan.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    /**
     * 从云数据库中取出数据后映射到对象中 方便操作
     */
    private String _id;
    private String create_time;
    private String nickname;
    private String password;
    private String username;
}
