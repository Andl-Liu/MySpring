package cn.andl.springframework.test.beans;

import java.util.HashMap;

public class UserDao {

    private static HashMap<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("1000", "壹零零零");
        hashMap.put("1001", "壹零零壹");
        hashMap.put("1002", "壹零零贰");
    }

    public String queryInfo(String uid) {
        return hashMap.get(uid);
    }
}
