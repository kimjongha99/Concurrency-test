package com.playdata.dblock.service;

import com.playdata.dblock.entity.Inventory;
import com.playdata.dblock.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
// 생성자 + 빌더로 생성하는걸 권장함. 인지적 오류 발생 가능성이 있음.
// 이 점은 lombok 공식문서에서 언급한 내용임.
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    //public void decrease(Long id, Long count){// 아이템 번호와 감소시킬 개수 입력
    public synchronized void decrease(Long id, Long count) {
        // 해당 번호 객체 가져옴
        Inventory inventory = inventoryRepository.findById(id).orElseThrow();

        inventory.decrease(count); // 갯수 감소 수행 후

        inventoryRepository.saveAndFlush(inventory); // 해당 개수를 디비에 재반영
    }


}



