package cn.rapdog.selenium.option;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.HashMap;

public class OptionsManager {

    /**
     * 获取浏览器配置
     *
     * @return
     */
    public static ChromeOptions getChromeOptions() {
        //System.setProperty("webdriver.chrome.driver", webDriverBean.getDriverPath());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        // 字符编码 utf-8 支持中文字符
        options.addArguments("lang=zh_CN.UTF-8");
        // 设置容许弹框
        options.addArguments("disable-infobars", "disable-web-security");
        // 驱动自动控制软件标识
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        // 设置无gui 开发时仍是不要加，能够看到浏览器效果
        //options.addArguments("--headless");
        options.addArguments("--disable-gpu");//禁止gpu渲染
        options.addArguments("--no-sandbox");//关闭沙盒模式
        options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--incognito"); // 隐身模式（无痕模式）
        options.addArguments("--disable-extensions"); // disabling extensions

        //禁用日志
        options.addArguments("--log-level=3");
        options.addArguments("--silent");

        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings", 2);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("blink-settings=imagesEnabled=false");//禁用图片
        //options.setBinary(webDriverBean.getChromepath());//手动指定使用的浏览器位置

        //设置代理
        //String proxyIP = proxyUtil.getProxyIP(true);
        //if (StrUtil.isNotEmpty(proxyIP)) {
        //    Proxy proxy = new Proxy().setHttpProxy(proxyIP).setSslProxy(proxyIP);
        //    options.setProxy(proxy);
        //}

        return options;
    }

    /**
     * 获取浏览器配置
     *
     * @return
     */
    public static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-logging");
        // 字符编码 utf-8 支持中文字符
        options.addArguments("lang=zh_CN.UTF-8");
        // 设置容许弹框
        options.addArguments("disable-infobars", "disable-web-security");
        // 驱动自动控制软件标识,这两条配置在edge不生效，已放弃适配edge
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        // 设置无gui 开发时仍是不要加，能够看到浏览器效果
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");//禁止gpu渲染
        options.addArguments("--no-sandbox");//关闭沙盒模式
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--incognito"); // 隐身模式（无痕模式）
        options.addArguments("--disable-extensions"); // disabling extensions

        //禁用日志
        options.addArguments("--log-level=3");
        options.addArguments("--silent");

        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings", 2);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("blink-settings=imagesEnabled=false");//禁用图片
        //options.setBinary(webDriverBean.getChromepath());//手动指定使用的浏览器位置

        //设置代理
        //String proxyIP = proxyUtil.getProxyIP(true);
        //if (StrUtil.isNotEmpty(proxyIP)) {
        //    Proxy proxy = new Proxy().setHttpProxy(proxyIP).setSslProxy(proxyIP);
        //    options.setProxy(proxy);
        //}

        return options;
    }
}
