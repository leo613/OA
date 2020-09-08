package com.taotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    public <T> T excute(Function<T,ShardedJedis> function){
        ShardedJedis shardedJedis=null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis= shardedJedisPool.getResource();
            return function.callback(shardedJedis);
        } finally {
            if (null!=shardedJedis){
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
    }


    /**
     * 执行 set操作
     */
    public String set(final String key, final String value){
       return excute(new Function<String, ShardedJedis>() {
           @Override
           public String callback(ShardedJedis shardedJedis) {
               return  shardedJedis.set(key,value);
           }
       });

    }


    /**
     * 执行 get操作
     *  从分片集群中读取数据
     */
    public String get(final String key){
        return excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }

    /**
     * 向集群中写入数据，并且指定生存时间
     * @param key
     * @param value
     * @param seconds
     * @return
     */
       public String set(final String key, final String value, final Integer seconds){
        return excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.setex(key,seconds,value);
            }
        });
     }

    /**+
     * 设置键值对的生存时间
     * @param key
     * @param seconds
     * @return
     */
     public Long expire(final String key, final Integer seconds){
           return excute(new Function<Long, ShardedJedis>() {
               @Override
               public Long callback(ShardedJedis shardedJedis) {
                   return shardedJedis.expire(key,seconds);
               }
           });
     }

    /**
     * 根据key删除指定的键值对
     * @param key
     * @return
     */
     public Long del(final String key){
         return excute(new Function<Long, ShardedJedis>() {
             @Override
             public Long callback(ShardedJedis shardedJedis) {
                 return shardedJedis.del(key);
             }
         });
     }
}
