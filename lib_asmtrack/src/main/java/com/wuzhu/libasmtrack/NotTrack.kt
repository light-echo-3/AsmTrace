package com.wuzhu.libasmtrack

/**
 * 配置此注解的类，不被插桩
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class NotTrack()
