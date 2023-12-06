github地址：https://github.com/light-echo-3/AsmTraceDemo

# 1.编译并发布插件  
## 1.1 修改插件版本
./AsmTrackPlugin/build.gradle:51<br>

![img_1.png](readme_imgs/img_1.png)

## 1.2 编译并发布到repo仓库  
./gradlew :AsmTrack:publishHelloAsmPublicationToMavenRepository
or<br>
![img.png](readme_imgs/img.png)

## 1.3 使用插件  
./build.gradle:10<br>
![img.png](readme_imgs/img_2.png)

# 2.调试插件
## 2.1.点击Edit Configurations
![img.png](readme_imgs/img_3.png)

## 2.2.点击+号，选择Remote
![img.png](readme_imgs/img_4.png)

## 2.3.随意输入名称，其他参数不要动
![img.png](readme_imgs/img_5.png)

## 2.4.然后在Terminal中输入如下命令
```
./gradlew --no-daemon -Dorg.gradle.debug=true :app:{taskName}
```
taskName是我们调试的task，比如assembleDebug、assembleRelease等  
例如：  
```
./gradlew --no-daemon -Dorg.gradle.debug=true clean :app:assembleDebug
```
输入命令后调试进程处于阻塞状态：
![img.png](readme_imgs/img_6.png)

## 2.5.最后在你想要调试的地方加上断点，点击调试按钮，这样就可以愉快的开始调试了。
![img.png](readme_imgs/img_7.png)
![img.png](readme_imgs/img_8.png)

## 2.6 测试插件是否发布成功
1. 修改log:com/wuzhu/asmtrack/AsmTrackPlugin.groovy:28
2. 发布插件
3. 执行命令：  
./gradlew testPluginPublishSuccess  
查看日志是否是最新的。

## 2.7 发布到github
[发布包到 GitHub Packages](https://docs.github.com/zh/actions/publishing-packages/publishing-java-packages-with-gradle#%E5%8F%91%E5%B8%83%E5%8C%85%E5%88%B0-github-packages)
[gradle发布jar到GitHub Packages & 使用](https://juejin.cn/post/7007289428158709797)

# 3 插件使用
## 3.1 初始化
在application中调用 com.wuzhu.libasmtrack.AsmTraceInitializer.init 


# 4 性能分析
[Perfetto入门](https://www.jianshu.com/p/f4cf101cc64f)<br>
[Perfetto官网](https://ui.perfetto.dev/)<br>
Perfetto trace 数据保存位置 /data/local/traces<br>
adb pull /data/local/traces<br>

![效果图](readme_imgs/img_9.png)

# TODO 