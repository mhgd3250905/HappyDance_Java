package com.example.permissionsutils;

import java.util.List;

/**
 * @author  :   shengkai
 * create   :   2020/8/19/019-0:07
 * package  :   com.example.permissionsutils
 * desc     :   权限本次被拒绝的结果
 */
public class RationaleResult extends PermissionResult{
    private List<String> permissions;

    public RationaleResult(List<String> permissions) {
        this.permissions = permissions;
    }
}
