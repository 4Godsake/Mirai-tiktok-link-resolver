package cn.rapdog;

import cn.rapdog.handler.TiktokShareMessageHandle;
import cn.rapdog.selenium.pool.WebDriverPool;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.File;

/**
 * @author rapdog
 */
public final class TiktokLinkResolver extends JavaPlugin {
    public static final TiktokLinkResolver INSTANCE = new TiktokLinkResolver();

    private TiktokLinkResolver() {
        super(new JvmPluginDescriptionBuilder("cn.rapdog.tiktok-link-resolver", "0.1.0")
                .name("tiktok-link-resolver")
                .info("抖音链接解析")
                .author("rapdog")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("tiktok-link-resolver Plugin loaded!");
        createDataFile();
        GlobalEventChannel.INSTANCE.registerListenerHost(new TiktokShareMessageHandle());
    }

    @Override
    public void onDisable() {
        getLogger().info("closing ChromeDrivePool...");
        WebDriverPool.getInstance().close();
        getLogger().info("tiktok-link-resolver Plugin quiting...");
    }

    public void createDataFile() {
        File mkdir = new File(TiktokLinkResolver.INSTANCE.getDataFolderPath().toString());
        if (!mkdir.exists()) {
            getLogger().info(TiktokLinkResolver.INSTANCE.getDataFolderPath()+"不存在");
            mkdir.mkdirs();
        }
    }
}