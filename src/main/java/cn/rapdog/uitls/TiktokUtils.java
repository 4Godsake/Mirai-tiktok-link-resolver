package cn.rapdog.uitls;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.rapdog.TiktokLinkResolver;
import cn.rapdog.exception.BusiException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String locationUrl = getHeader(shareUrl, "Location");
        checkStringNotEmpty(locationUrl, "地址解析异常");
        String videoId = getFirst(locationUrl);
        checkStringNotEmpty(videoId, "视频id解析异常");
        String url2 = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";
        String jsonString;
        try (HttpResponse resp = HttpRequest.get(url2 + videoId)
                .header(Header.USER_AGENT, "Hutool http")
                .timeout(10000)
                .execute()) {
            jsonString = resp.body();
        } catch (Exception e) {
            throw new BusiException(e.getMessage());
        }
        String url3 = getVideoAddrBeforeLocation(jsonString);
        checkStringNotEmpty(url3, "地址解析异常");
        return getHeader(url3, "Location");
    }

    public static void checkStringNotEmpty(String str, String erroMessage) throws BusiException {
        if (StrUtil.isEmpty(str)) {
            throw new BusiException(erroMessage);
        }
    }

    private static String getHeader(String url, String header) {
        try (HttpResponse resp = HttpRequest.get(url)
                .header(Header.USER_AGENT, "Hutool http")
                .timeout(10000)
                .execute()) {
            return resp.header(header);
        } catch (Exception e) {
            throw new BusiException("地址解析过程异常:" + e.getMessage());
        }
    }

    private static String getVideoAddrBeforeLocation(String jsonString) {
        String addr = "";
        List<JSONObject> list = (List<JSONObject>) JSON.parseObject(jsonString).get("item_list");
        if (CollectionUtil.isNotEmpty(list)) {
            JSONObject video = (JSONObject) list.get(0).get("video");
            JSONObject playAddr = (JSONObject) video.get("play_addr");
            List<String> urlList = (List<String>) playAddr.get("url_list");
            addr = CollectionUtil.isNotEmpty(list) ? urlList.get(0).replace("/playwm/", "/play/") : "";
        }
        return addr;
    }

    /**
     * 利用其预编译功能，加快正则匹配速度
     */
    public static Pattern dynamic = Pattern.compile(".*video/(\\d+)/?.*");
    private static Pattern dynamicLimitCount = Pattern.compile(".*video/(\\d+)/?.*");

    private static String getFirst(String content) {
        Matcher m = dynamicLimitCount.matcher(content);
        if (m.find()) {
            return m.group(1);
        } else {
            return "";
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
