package com.playdata.dblock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class LettuceRepository {

    //레디스의 요소는 키 , 밸류 둘다 string
    private RedisTemplate<String,String> redisTemplate;

    public Boolean lock (Long key){
        //레디스에 키가 존재하지않으면 새로 만들어주는 명령어
        return redisTemplate.opsForValue().setIfAbsent(genrateKey(key),"lock", Duration.ofMillis(3000));
    }

    public Boolean unlock(Long key){
        //지우기
        return redisTemplate.delete(genrateKey(key));
    }

    private String genrateKey(Long key) {
        return key.toString();
    }

}
