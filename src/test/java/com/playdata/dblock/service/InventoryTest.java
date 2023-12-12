package com.playdata.dblock.service;

import com.playdata.dblock.entity.Inventory;
import com.playdata.dblock.repository.InventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest // 통합 테스트, 스프링 컨테이너를 생성해서 테스트 코드가 돌아갑니다.
// 만약 단위 테스트로 진행하고 싶은경우는 Mock객체를 만들어서 수행합니다.
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach // 실제 테스트 돌리기 전 아이템 1개, 1번Id 부여, 100개 재고로 집어넣기
    public void insert() {
        Inventory inventory = new Inventory(1L, "산타복", 100L);
        inventoryRepository.saveAndFlush(inventory);
    }

    @AfterEach // 테스트 수행 후 디비를 초기화(비워버리기)
    public void delete(){
        inventoryRepository.deleteAll();
    }

    @Test
    @DisplayName("100개의 재고를 가진 1번아이템을 1개 감소시키면 99개가 남는다")
    public void 동시성문제가생기지않는재고감소상황() {
        // given(없음)

        // when
        inventoryService.decrease(1L, 1L);

        // then
        Inventory inventory = inventoryRepository.findById(1L).orElseThrow();

        // import static org.junit.jupiter.api.Assertions.assertEquals;
        assertEquals(99, inventory.getCount());
    }

    @Test
    @DisplayName("멀티스레드를 활용해서 동시에 100명이 1개씩 주문을 넣는 상황")
    public void 동시에100명이주문하는상황() throws InterruptedException { // 멀티스레드 활용이므로 예외처리
        // 동시 요청의 개수
        final int threadCount = 100;
        // 32쓰레드가 동시에 요청넣도록 풀 설정
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 요청 마친 쓰레드는 전체 쓰레드 풀이 끝날때까지 대기하도록 처리
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i = 0; i < 100; i++){// 반복문으로 100회 요청
            executorService.submit(() -> { // 개별 쓰레드가 호출할 요청을 람다로 작성
                try {
                    inventoryService.decrease(1L, 1L);
                }finally {
                    countDownLatch.countDown(); // 요청 들어간 쓰레드는 대기상태로 전환
                }
            });
        }

        countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체를 종료

        // 재고가 100개인데 1개 감소 요청을 100번 넣었으므로 0개가 남아야 함.
        Inventory inventory = inventoryRepository.findById(1L).orElseThrow();

        // 1번 아이템의 재고량은 0개일것이라고 단언
        assertEquals(0, inventory.getCount());

    }




}





