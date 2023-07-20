package cn.andl.springframework.beans.factory;

/**
 * 提供销毁操作的接口
 */
public interface DisposableBean {

    /**
     * 销毁前调用
     */
    void destroy() throws Exception;

}
