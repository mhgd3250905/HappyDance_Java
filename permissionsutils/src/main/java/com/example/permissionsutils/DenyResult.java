package com.example.permissionsutils;

import java.util.List;

/**
 * @author  :   shengkai
 * create   :   2020/8/19/019-0:07
 * package  :   com.example.permissionsutils
 * desc     :   权限拒绝且不再弹出的标志
 */
public class DenyResult extends PermissionResult{
    private List<String> permissions;

    public DenyResult(List<String> permissions) {
        this.permissions = permissions;
    }
}
