package com.playdata.dblock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory { // 재고관리용 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 물품번호

    private String itemName; // 물품명

    private Long count; // 재고량


    @Version
    private Long version; // 버전.  낙관적에서 정합성을 맞추기 위해 선언.



    // 재고가 0개 미만으로 떨어지지 않도록 검증해주는 메서드
    public void decrease(Long count){
        if(this.count - count < 0){
            throw new RuntimeException("재고량이 부족해 판매할 수 없습니다.");
        }
        this.count -= count;
    }

}
