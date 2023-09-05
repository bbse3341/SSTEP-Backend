package com.sstep.demo.checklist.dto;

import com.sstep.demo.checklistmanager.dto.CheckListManagerRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CheckListRequestDto {
    private String title; //체크 리스트 제목
    private String date; //체크 리스트 작성 일자
    private String contents; //체크 리스트 내용
    private String endDay; //체크 리스트 마감 일자
    private String categoryName; //카테고리 명
    private Set<CheckListManagerRequestDto> checkListManagersRequestDto; //체크 리스트 담당자 배열
    private boolean needPhoto; //체크 리스트 사진 필수 여부
    private boolean isComplete; //체크 리스트 완료 여부
    private String memo; //체크 리스트 완료 시 메모
    private long[] photoId; //사진 고유 번호 배열
    private long categoryId; //카테고리 고유번호
}
