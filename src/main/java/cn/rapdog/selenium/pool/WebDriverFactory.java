package cn.rapdog.selenium.pool;

import cn.rapdog.selenium.option.OptionsManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {

    private ChromeOptions options;

    public WebDriverFactory(ChromeOptions options){
        WebDriverManager.chromedriver().setup();
        this.options = options;
    }

    private static class SingletonHolder {
        private static final WebDriverFactory SINGLETON = new WebDriverFactory(OptionsManager.getChromeOptions());
    }

    /**
     * 单例方法入口
     * @return 单例类
     */
    public static WebDriverFactory getInstance() {
        return SingletonHolder.SINGLETON;
    }


    public WebDriver create() throws Exception {
        return new ChromeDriver(options);
    }


    public PooledObject<WebDriver> wrap(WebDriver obj) {
        return new DefaultPooledObject<>(obj);
    }

    /**
     * No-op.
     *
     * @param p ignored
     */
    @Override
    public void activateObject(PooledObject<WebDriver> p) throws Exception {
        super.activateObject(p);
    }

    /**
     * No-op.
     *
     * @param p ignored
     */
    @Override
    public void destroyObject(PooledObject<WebDriver> p) throws Exception {
        p.getObject().quit();
    }

    @Override
    public PooledObject<WebDriver> makeObject() throws Exception {
        return super.makeObject();
    }

    /**
     * No-op.
     *
     * @param p ignored
     */
    @Override
    public void passivateObject(PooledObject<WebDriver> p) throws Exception {
        super.passivateObject(p);
    }

    /**
     * Always returns {@code true}.
     *
     * @param p ignored
     * @return {@code true}
     */
    @Override
    public boolean validateObject(PooledObject<WebDriver> p) {
        return super.validateObject(p);
    }

    public ChromeOptions getOptions() {
        return options;
    }

    public void setOptions(ChromeOptions options) {
        this.options = options;
    }
}
