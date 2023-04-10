github地址：https://github.com/hardy666666/AsmTrackDemo

# 1.编译并发布插件  
## 1.1 修改插件版本
./AsmTrack/build.gradle:46<br>

![img_1.png](readme_imgs/img_1.png)

## 1.2 编译并发布到repo仓库  
./gradlew :AsmTrack:publishHelloAsmPublicationToMavenRepository<br> 
or<br>
![img.png](readme_imgs/img.png)

## 1.3 使用插件  
./build.gradle:10<br>
![img.png](readme_imgs/img_2.png)
