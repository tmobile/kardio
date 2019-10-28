/*******************************************************************************
 * Copyright 2019 T-Mobile USA, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * See the LICENSE file for additional language around disclaimer of warranties.
 * Trademark Disclaimer: Neither the name of "T-Mobile, USA" nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 ******************************************************************************/
package com.tmobile.kardio.util;

import com.tmobile.kardio.bean.User;
import com.tmobile.kardio.dao.AppSessionDao;
import com.tmobile.kardio.exceptions.LoginFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * 
 * Class to manage LDAP authentication for a user who is trying to login
 * 
 */
@Service
@PropertySource("classpath:application.properties")
public class LDAPAuthUtil {
    private static Logger log = LoggerFactory.getLogger(LDAPAuthUtil.class);

    @Value("${ldap.url}")
    private String ldapURL;

    @Value("${ldap.domain}")
    private String ldapDomain;

    @Value("${ldap.searchBase}")
    private String searchBase;

    @Value("${login.enable.ldap}")
    private String loginEnableLdap;

    @Value("${login.admin.username}")
    private String loginAdminUserName;

    @Value("${login.admin.password}")
    private String loginAdminPassword;

    @Autowired
    private AppSessionDao appSessionDao;

    /**
     * Function to authenticate the user trying to log in
     * 
     * @param username
     * @param password
     * @return User
     * @throws NamingException
     */
    public User authenticateAndGetGroups(String username, String password) throws NamingException {
        if (loginEnableLdap != null && !loginEnableLdap.equalsIgnoreCase("true")) {
            return doStaticAuth(username, password);
        }

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.PROVIDER_URL, ldapURL);
        env.put(Context.SECURITY_PRINCIPAL, username + "@" + ldapDomain);
        env.put(Context.SECURITY_CREDENTIALS, password);

        DirContext context = null;
        try {
            context = new InitialDirContext(env);
        } catch (Exception e) {
            if (context != null) {
                context.close();
            }
            log.error("LDAP auth Failed", e);
            throw new LoginFailedException("Invalid User Id and Password");
        }

        String searchString = "CN=" + username;
        User returnUser = new User();
        returnUser.setUserId(username);
        Set<String> memberships = new HashSet<>();
        returnUser.setUserGroups(memberships);

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[] { "*", "+" });
        NamingEnumeration<SearchResult> renum = context.search(searchBase, searchString, controls);

        if (!renum.hasMore()) {
            return returnUser;
        }
        SearchResult result = renum.next();

        Attributes attributes = result.getAttributes();
        Map<Integer, String> listOfRoles = appSessionDao.getAllAppRoles();
        if (listOfRoles == null) {
            log.error("No Roles available for this user");
            throw new LoginFailedException("No Roles available for this user");
        }
        int numberOfRoles = 0;
        NamingEnumeration<String> allAttributeKey = attributes.getIDs();
        while (allAttributeKey.hasMoreElements()) {
            String key = allAttributeKey.nextElement();
            if (key.equals("memberOf")) {
                Attribute attribute = attributes.get(key);
                for (int i = 0; i < attribute.size(); i++) {
                    String attributeValue = (String) attribute.get(i);
                    String role = attributeValue.substring(attributeValue.indexOf("=") + 1, attributeValue.indexOf(","));
                    if (listOfRoles.containsValue(role)) {
                        numberOfRoles++;
                    }

                    if (listOfRoles.get(0).equalsIgnoreCase(role)) {
                        returnUser.setAdmin(true);
                    }
                    memberships.add(role);
                }
            }
            if (key.equals("displayName")) {
                Attribute attribute = attributes.get(key);
                for (int i = 0; i < attribute.size(); i++) {
                    String attributeValue = (String) attribute.get(i);
                    returnUser.setUserName(attributeValue);
                }
            }
            if (key.equals("userPrincipalName")) {
                Attribute attribute = attributes.get(key);
                for (int i = 0; i < attribute.size(); i++) {
                    String attributeValue = (String) attribute.get(i);
                    returnUser.setEmailId(attributeValue);
                }
            }
        }
        if (numberOfRoles == 0) {
            context.close();
            log.error("Required Roles not available for this user");
            throw new LoginFailedException("Required Roles not available for this user");
        }

        context.close();
        return returnUser;
    }

    /**
     * Validate the username / password from property file.
     * 
     * @param username
     * @param password
     * @return
     */
    private User doStaticAuth(String username, String password) {
        if (loginAdminUserName == null || loginAdminUserName.trim().length() == 0 || loginAdminPassword == null
                || loginAdminPassword.trim().length() == 0) {
            log.error("User ID and password is not configured");
            throw new LoginFailedException("User ID and password is not configured");
        }
        if (username == null || username.trim().length() == 0 || password == null || password.trim().length() == 0
                || !loginAdminUserName.equalsIgnoreCase(username) || !loginAdminPassword.equals(password)) {
            log.error("User ID or password is not correct");
            throw new LoginFailedException("User ID or password is not correct");
        }
        User returnUser = new User();
        returnUser.setUserId(username);
        returnUser.setUserName(username);
        returnUser.setAdmin(true);
        return returnUser;
    }
}
