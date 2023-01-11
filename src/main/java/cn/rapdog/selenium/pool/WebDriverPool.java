package cn.rapdog.selenium.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;

public class WebDriverPool extends GenericObjectPool<WebDriver> {

    private static class SingletonHolder {
        private static final WebDriverPool SINGLETON = new WebDriverPool(WebDriverFactory.getInstance(),PoolProperties.getPoolConfig());
    }

    /**
     * 单例方法入口
     * @return 单例类
     */
    public static WebDriverPool getInstance() {
        return SingletonHolder.SINGLETON;
    }

    public WebDriverPool(PooledObjectFactory<WebDriver> factory) {
        super(factory);
    }

    public WebDriverPool(PooledObjectFactory<WebDriver> factory, GenericObjectPoolConfig<WebDriver> config) {
        super(factory, config);
    }

    public WebDriverPool(PooledObjectFactory<WebDriver> factory, GenericObjectPoolConfig<WebDriver> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
