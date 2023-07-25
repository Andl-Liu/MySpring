package cn.andl.springframework.beans.context.support;

/**
 * 从类路径加载XML资源的应用上下文具体实现类
 * 具体对外给用户提供应用上下文的方法
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {

    }

    /**
     * 从XML中加载Bean定义，并刷新上下文
     * @param configLocation 资源路径
     */
    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    /**
     * 从XML中加载Bean定义，并刷新上下文
     * @param configLocations 资源路径
     */
    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
