package com.playdata.dblock.service;


import com.playdata.dblock.entity.Inventory;
import com.playdata.dblock.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OptimisticInventoryService {

    private  final InventoryRepository inventoryRepository;// 레포지토리 주입


    @Transactional
    public void decrease(Long id, Long count){ // 아이템번호와 감소시킬 수량을 적으면
        // 해당 entity가 saveAndFlush()되어서 제어권을 반납하기 전까지는 동시 접근 불가
        Inventory inventory = inventoryRepository.findByIdOptimistic(id);

        inventory.decrease(count);

        inventoryRepository.saveAndFlush(inventory); // 디비 반영 및 락 풀기
    }


}
