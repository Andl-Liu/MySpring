package cn.andl.springframework.test.beans;

public class UserService {
    UserDao userDao;

    String uid;

    public void queryInfo() {
        System.out.println("查询信息： " + userDao.queryInfo(uid));
    }
}
