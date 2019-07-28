package com.wenchao.superorm;

/**
 * @author wenchao
 * @date 2019/7/28.
 * @time 17:29
 * description：
 */
@DbTable("tb_user")
public class User {

    @DbField("id")
    private Integer id;
    @DbField("username")
    private String username;
    @DbField("password")
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
