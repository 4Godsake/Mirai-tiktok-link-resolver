package cn.rapdog.handler;


import cn.rapdog.TiktokLinkResolver;
import cn.rapdog.utils.TiktokUtils;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rapdog
 */
public class TiktokShareMessageHandle extends SimpleListenerHost {

    private static final MiraiLogger LOGGER = TiktokLinkResolver.INSTANCE.getLogger();


    @EventHandler()
    public ListeningStatus onBotGroupRequest(BotInvitedJoinGroupRequestEvent event) {
        //收到邀请
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onFriendRequest(NewFriendRequestEvent event) {
        return ListeningStatus.LISTENING;
    }


    @EventHandler()
    public ListeningStatus addFriendRequest(FriendAddEvent event) {
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus OnGroupTempMessageEvent(GroupTempMessageEvent event) {
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {
        return ListeningStatus.LISTENING;
    }


    //EventHandler可以指定多个属性，包括处理方式、优先级、是否忽略已取消的事件
    //其默认值请见EventHandler注解类
    //因为默认处理的类型为Listener.ConcurrencyKind.CONCURRENT
    //需要考虑并发安全
    @EventHandler()
    public ListeningStatus onGroupMessage(GroupMessageEvent event) {
        groupMessageHandle(event);
        //保持监听
        return ListeningStatus.LISTENING;
    }

    //处理在处理事件中发生的未捕获异常
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }

    /**
     * 视频消息在linux中需要加以下启动参数才能发送
     * `-Dmirai.message.allow.sending.file.message=true`
     * @param event
     */
    private static void groupMessageHandle(GroupMessageEvent event) {
        String messageStr = event.getMessage().contentToString();
        if (messageStr.contains("v.douyin.com")) {
            LOGGER.info("收到抖音分享消息：" + messageStr);
            event.getGroup().sendMessage("开始解析，耗时较长，请耐心等待");
            try {
                String shareUrl = shareTextToUrl(messageStr);
                TiktokUtils.checkStringNotEmpty(shareUrl, "分享链接解析失败，请检查链接是否正确");
                String downLoadPath = Paths.get(TiktokLinkResolver.INSTANCE.getDataFolderPath().toString(),
                        event.getSender().getId()+"的分享" + System.currentTimeMillis() + ".mp4").toString();
                File video = TiktokUtils.downloadByShareUrl(shareUrl, downLoadPath);
                LOGGER.info("视频下载成功，缓存目录：{"+downLoadPath+"}文件位置：{"+video.getAbsolutePath()+"} —— 文件大小：{"+video.getTotalSpace()+"}");
                try (ExternalResource resource = ExternalResource.create(video)) {
                    event.getGroup().getFiles().getRoot().uploadNewFile("/"+video.getName(), resource);
                } catch (Exception e) {
                    LOGGER.error("发送群文件异常：" + e.getMessage());
                    event.getGroup().sendMessage("发送群文件异常：" + e.getMessage());
                } finally {
                    if (!video.delete()) {
                        LOGGER.error("视频缓存文件删除失败,文件路径：" + video.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                event.getGroup().sendMessage("视频解析失败:[" + e.getMessage() + "]");
            }
        }
    }

    public static String shareTextToUrl(String shareText) {
        String regex = "(http:|https:)//v.douyin.com/[A-Za-z\\d._?%&+\\-=/#]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(shareText);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}
