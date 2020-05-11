package com.rains.graphql.common.authentication;

import java.util.Set;

public interface IUserManager {
    Set<String> getUserRoles(String username);

    Set<String> getUserPermissions(String username);

    String getPasswordByUserName(String username);
}
