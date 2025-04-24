#!/bin/bash
./gradlew :AsmTrackPlugin80:jreleaserConfig # 验证配置文件是否正确
./gradlew :AsmTrackPlugin80:clean
./gradlew :AsmTrackPlugin80:publishMavenAsmTracePublicationToBuildRepoRepository  # 发布到本地 build/staging-deploy
./gradlew :AsmTrackPlugin80:jreleaserFullRelease # 发布到远程Maven中央仓库
