#!/bin/bash
./gradlew :AsmTrackPlugin:jreleaserConfig # 验证配置文件是否正确
./gradlew :AsmTrackPlugin:clean
./gradlew :AsmTrackPlugin:publishMavenAsmTracePublicationToBuildRepoRepository  # 发布到本地 build/staging-deploy
./gradlew :AsmTrackPlugin:jreleaserFullRelease # 发布到远程Maven中央仓库
