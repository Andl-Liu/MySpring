package cn.andl.springframework.test.bean;

import java.util.HashMap;

public class UserDao {

    private static HashMap<String, String> hashMap = new HashMap<>();

    public void initDataMethod() {
        System.out.println("执行 init-method ");
        hashMap.put("1000", "壹零零零");
        hashMap.put("1001", "壹零零壹");
        hashMap.put("1002", "壹零零贰");
    }

    public void destroyDataMethod() {
        System.out.println("执行 destroy-method ");
        hashMap.clear();
    }

    public String queryUserName(String uid) {
        return hashMap.get(uid);
    }
}
