#!/bin/bash
./gradlew :lib_asmtrack:assemble
./gradlew :lib_asmtrack:publishReleasePublicationToBuildRepoRepository  # 发布到本地 build/staging-deploy
#接下来，打zip包，手动发布到maven central