# Mirai-tiktok-link-resolver
Mirai-console插件，用于解析下载某音视频分享链接中无水印视频
项目地址：https://github.com/4Godsake/Mirai-tiktok-link-resolver
下载地址：https://github.com/4Godsake/Mirai-tiktok-link-resolver/releases/tag/release-0.1.0
## 开始使用

插件需要以 [Mirai-Console](https://github.com/mamoe/mirai)

为基础，你可以下载 [MCL](https://github.com/iTXTech/mirai-console-loader/releases) 作为你的Mirai插件载入器

## 使用方法


将插件放到MCL的plugin目录下，启动mirai-console即可使用，群中有抖音分享链接消息时会自动解析视频并上传

linux环境下，mirai-console需要加上以下启动参数
```
-Dmirai.message.allow.sending.file.message=true
```

目前仅实现基本功能，后续会持续更新

### 更新计划：
* 配置功能开启的群
* 可配置视频消息通过合并转发的方式发出
* 可配置视频上传目录
* 可配置定期清理此插件在群文件中上传的文件

*如果此插件对您有帮助，请不要吝啬github的星星，您的支持是我持续更新的动力*