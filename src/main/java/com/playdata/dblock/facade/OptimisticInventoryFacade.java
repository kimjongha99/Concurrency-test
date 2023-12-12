package com.playdata.dblock.facade;


import com.playdata.dblock.service.OptimisticInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticInventoryFacade {
    // 아래작업을 퍼사드로 작성한 이유는 서비스 레이어에서는 100프로 돌아가는 서비스를 넣어야한다고 생각



    //퍼사드 클래스의 역할은 낙관적 서비스의 decrease() 메소드가 반영될때까지 지속호출
    //Service객체에 매핑
    private final OptimisticInventoryService optimisticInventoryService;


    public void decrease(Long id, Long count) throws InterruptedException {
        // 낙관적 서비스의 decrease() 메소드가 반영될때까지 지속호출
        while(true){
            try{
                optimisticInventoryService.decrease(id, count);
                break;
            }catch (Exception e){
                // 낙관적 락이 발생하면 다시 시도 (버전이 맞지않아 예외발생시)
                Thread.sleep(100); // 0.1초 대기후 다시시도
            }
        }
    }



}
