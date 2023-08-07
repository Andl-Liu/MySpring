package cn.andl.springframework.test.bean;

public class Wife {

    private Husband husband;
    private IMother mother;

    public String queryHusband() {
        return "Wife.husband、Mother.callMother：" + mother.callMother();
    }

}
