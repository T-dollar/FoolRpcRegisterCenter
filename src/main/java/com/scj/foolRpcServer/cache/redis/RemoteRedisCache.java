package com.scj.foolRpcServer.cache.redis;

import com.scj.foolRpcServer.constant.FRSConstant;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author suchangjie.NANKE
 * @Title: RedisCache
 * @date 2023/8/29 22:53
 * @description 使用redis实现存储
 */

@Component
public class RemoteRedisCache {

    @Autowired
    private RedisOpe redisOpe;

    /**
     * 信息存储
     * @param app 应用名
     * @param version 版本
     * @param ip_port 下游主机的IP+PORT
     * @param className 服务类名称
     * @return 该 channel and ip 是否首次添加
     */
    public boolean save(String app, String version
            , String ip_port, String className, Channel channel){
        String app_version = app + FRSConstant.UNDER_LINE + version;
        String class_version = className + FRSConstant.UNDER_LINE + version;
        // 存储 app:class
        redisOpe.cacheMap(FRSConstant.REDIS_PRE + FRSConstant.APP + app_version
                , class_version, String.valueOf(0));
        // 存储 class:app
        redisOpe.cacheMap(FRSConstant.REDIS_PRE + FRSConstant.CLASS, class_version, app_version);
        // 存储数据到 app_ipList
        redisOpe.cacheZSet(FRSConstant.REDIS_PRE + FRSConstant.IP_LIST + app_version, ip_port);
        // channel_ipPort 存储
        redisOpe.cacheMap(FRSConstant.REDIS_PRE + FRSConstant.CHANNEL
                , channel.id().asLongText(), ip_port);
        return true;
    }
}
