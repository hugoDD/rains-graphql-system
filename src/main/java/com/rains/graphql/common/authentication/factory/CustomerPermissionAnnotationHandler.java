package com.rains.graphql.common.authentication.factory;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * Created by wbzhongsy on 2018/8/29.
 */
public class CustomerPermissionAnnotationHandler extends PermissionAnnotationHandler {
    public CustomerPermissionAnnotationHandler() {
    }

    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (a instanceof RequiresPermissions) {
            RequiresPermissions r = (RequiresPermissions) a;
            int rlength = r.value().length;
            if (r.value() != null && rlength > 0) {
                String[] perms = r.value();
                String[] insertPerms = new String[rlength];
                Subject subject = SecurityUtils.getSubject();
                if (perms.length == 1) {
                    insertPerms = perms;
                } else if (Logical.AND.equals(r.logical())) {

                    String[] var6 = perms;
                    int var7 = perms.length;

                    for (int var8 = 0; var8 < var7; ++var8) {
                        String permission = var6[var8];
                        if (subject.isPermitted(permission)) {
                            insertPerms[var8] = permission;
                        }
                    }
                } else {
                    if (Logical.OR.equals(r.logical())) {

                        String[] var6 = perms;
                        int var7 = perms.length;

                        for (int var8 = 0; var8 < var7; ++var8) {
                            String permission = var6[var8];
                            if (subject.isPermitted(permission)) {
                                insertPerms[var8] = permission;
                            }
                        }

                    }
                }

                int var9 = insertPerms.length;
                for (int var10 = 0; var10 < var9; ++var10) {
                    String permissionStr = insertPerms[var10];
                    if (permissionStr != null) {
                        String[] insertStr = permissionStr.split(":");

                    }

                }

            }
            RequiresPermissions rpAnnotation = (RequiresPermissions) a;
            String[] perms = this.getAnnotationValue(a);
            Subject subject = this.getSubject();
            if (perms.length == 1) {
                subject.checkPermission(perms[0]);
            } else if (Logical.AND.equals(rpAnnotation.logical())) {
                this.getSubject().checkPermissions(perms);
            } else {
                if (Logical.OR.equals(rpAnnotation.logical())) {
                    boolean hasAtLeastOnePermission = false;
                    String[] var6 = perms;
                    int var7 = perms.length;

                    for (int var8 = 0; var8 < var7; ++var8) {
                        String permission = var6[var8];
                        if (this.getSubject().isPermitted(permission)) {
                            hasAtLeastOnePermission = true;
                        }
                    }

                    if (!hasAtLeastOnePermission) {
                        this.getSubject().checkPermission(perms[0]);
                    }
                }

            }
        }
    }
}
