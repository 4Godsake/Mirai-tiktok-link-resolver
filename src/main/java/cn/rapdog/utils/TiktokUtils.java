package cn.rapdog.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.rapdog.TiktokLinkResolver;
import cn.rapdog.exception.BusiException;
import cn.rapdog.selenium.pool.WebDriverPool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.utils.MiraiLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author rapdog
 */
public class TiktokUtils {

    private static final MiraiLogger LOGGER = TiktokLinkResolver.INSTANCE.getLogger();

    public static File downloadByShareUrl(String url, String downloadPath) throws MalformedURLException {
        TiktokUtils.checkStringNotEmpty(url, "分享链接解析失败，请检查链接是否正确");
        String videoUrl = TiktokUtils.getRealUrl(url);
        TiktokUtils.checkStringNotEmpty(videoUrl, "暂不支持图文视频解析，快去催我更新！");
        return TiktokUtils.downloadNet(videoUrl, downloadPath);
    }

    private static String getRealUrl(String shareUrl) throws BusiException {
        LOGGER.info("原抖音分享链接为:" + shareUrl);
        String videoUrl = "";
        try (WebDriverPool webDriverPool = WebDriverPool.getInstance()) {
            WebDriver driver = null;
            try {
                driver = webDriverPool.borrowObject();
                driver.get(shareUrl);
                WebElement element = driver.findElement(By.id("RENDER_DATA"));
                String jsonStr = URLUtil.decode(element.getAttribute("innerHTML"));
                JSONObject jo = JSON.parseObject(jsonStr);
                videoUrl = jo.getObject("42", JSONObject.class)
                             .getObject("aweme", JSONObject.class)
                             .getObject("detail", JSONObject.class)
                             .getObject("video", JSONObject.class)
                             .get("playApi").toString().replace("//", "");
                LOGGER.info("抖音分享链接解析结果:" + videoUrl);
            } catch (Exception e) {
                LOGGER.error("抖音分享链接解析失败", e);
            } finally {
                if (driver != null) {
                    webDriverPool.returnObject(driver);
                }
            }
        }
        return videoUrl;
    }

    public static void checkStringNotEmpty(String str, String erroMessage) throws BusiException {
        if (StrUtil.isEmpty(str)) {
            throw new BusiException(erroMessage);
        }
    }

    private static File downloadNet(String linkUrl, String downloadPath) throws MalformedURLException {
        // 下载网络文件
        int byteRead = 0;
        URL url = new URL(linkUrl);
        try {
            URLConnection conn = url.openConnection();
            try (FileOutputStream fs = new FileOutputStream(downloadPath);
                 InputStream inStream = conn.getInputStream()) {
                byte[] buffer = new byte[1204];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                return new File(downloadPath);
            }
        } catch (IOException e) {
            throw new BusiException("视频下载失败:" + e.getMessage());
        }
    }
}
