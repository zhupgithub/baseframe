package com.zhupeng.baseframe.utils;

import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 
 * ClassName: RedisUtil 
 * @Description: redis相关工具方法
 * @author zhupeng
 * @date 2019年1月2日
 */
@Component
@Scope("singleton")
public class RedisUtil {

	@Autowired
	private static StringRedisTemplate redisTemplate;

    //静态注入
	@Autowired
	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		RedisUtil.redisTemplate = redisTemplate;
	}

	/**
	 * 
	 * @Description: 判断redis中是否存在相关数据，当redis中没有相关数据，将 查询数据库中，然后插入redis中
	 * @param @param key 查询redis和插入数据中key中的键
	 * @param @return 
	 * @param @throws Exception   
	 * @return String  
	 * @throws
	 * @author zhupeng
	 * @date 2019年1月2日
	 */
	public static String getOrSetValueByKey(String key) throws Exception{
		
		//判断redis中是否存在该键值
		boolean weChatLoginUrl = redisTemplate.hasKey(key);
		if(weChatLoginUrl){
			return (String) redisTemplate.opsForValue().get(key);
		}
		
		//获得数据
		String value = null;//weChatConfMapper.getValueByKey(key);
		if(StringUtil.isEmpty(value)){
			throw new Exception("微信系统表中没有设置该属性值,key:"+key);
		}
		//将系统中的数据添加到redis中
		redisTemplate.opsForValue().set(key, value);
		return value;
	}
	
	/**
	 * 
	 * @Description: 向redis中存入数据
	 * @param @param key 键
	 * @param @param value 值   
	 * @return void  
	 * @throws
	 * @author zhupeng
	 * @date 2019年1月3日
	 */
	public static void setValueAndKey(String key , String value,Long timeout ,TimeUnit timeUnit){
		if(key == null){
			throw new RuntimeException("传入redis中的key不能为空");
		}
		if(timeUnit == null){
			timeUnit = TimeUnit.SECONDS;
		}
		redisTemplate.opsForValue().set(key, value,timeout,timeUnit);
	}
	
	/**
	 * 
	 * @Description: 通过redis的key获得value数据
	 * @param @param key 键
	 * @return void  
	 * @throws
	 * @author zhupeng
	 * @date 2019年1月3日
	 */
	public static String getValueByKey(String key){
		if(key == null){
			throw new RuntimeException("传入redis中的key不能为空");
		}
		//判断redis中是否存在该键值
		boolean weChatLoginUrl =redisTemplate.hasKey(key);
		if(weChatLoginUrl){
			return (String) redisTemplate.opsForValue().get(key);
		}
		return null;
	}

	public static void deleteByKey(String key) {
		if(key == null){
			throw new RuntimeException("传入redis中的key不能为空");
		}
		redisTemplate.delete(key);
	}
	

}
