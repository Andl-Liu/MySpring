package cn.andl.springframework.test.beans;

public class UserService {

    private String name;

    public UserService(String name) {
        this.name = name;
    }

    public UserService(Integer integer) {
        this.name = "integer";
    }

    public void queryUserInfo() {
        System.out.println("查询用户信息：" + name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("").append(name);
        return sb.toString();
    }

}
