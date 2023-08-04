package com.sstep.demo.checklist.service;

import com.sstep.demo.category.CategoryMapper;
import com.sstep.demo.category.domain.Category;
import com.sstep.demo.category.dto.CategoryRequestDto;
import com.sstep.demo.checklist.CheckListMapper;
import com.sstep.demo.checklist.CheckListRepository;
import com.sstep.demo.checklist.domain.CheckList;
import com.sstep.demo.checklist.dto.CheckListRequestDto;
import com.sstep.demo.checklistmanager.domain.CheckListManager;
import com.sstep.demo.checklistmanager.dto.CheckListManagerRequestDto;
import com.sstep.demo.checklistmanager.service.CheckListManagerService;
import com.sstep.demo.photo.domain.Photo;
import com.sstep.demo.photo.service.PhotoService;
import com.sstep.demo.staff.StaffRepository;
import com.sstep.demo.staff.domain.Staff;
import com.sstep.demo.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckListService {
    private final CheckListRepository checkListRepository;
    private final StaffRepository staffRepository;
    private final CheckListMapper checkListMapper;
    private final StoreService storeService;
    private final CategoryMapper categoryMapper;
    private final PhotoService photoService;
    private final CheckListManagerService checkListManagerService;


    public void saveCheckList(Long staffId, CheckListRequestDto checkListRequestDto, CategoryRequestDto[] categoryRequestDto, CheckListManagerRequestDto[] checkListManagerRequestDto) {
        Staff staff = staffRepository.findById(staffId).orElseThrow();

        //직원 고유번호로 해당 직원이 작성한 체크 리스트들 가져오기
        List<CheckList> checkLists = getCheckListsByStaffId(staffId);
        CheckList checkList = getCheckListEntity(checkListRequestDto);

        List<Category> categories = new ArrayList<>();
        for (CategoryRequestDto categoryDto : categoryRequestDto) {
            categories.add(categoryMapper.toCategoryEntity(categoryDto));
        }
        checkList.setCategories(categories);

        List<CheckListManager> checkListManagers = new ArrayList<>();
        for (CheckListManagerRequestDto managerRequestDto : checkListManagerRequestDto) {
            checkListManagers.add(checkListManagerService.saveManagers(managerRequestDto));
        }
        checkList.setCheckListManagers(checkListManagers);

        checkLists.add(checkList);
        staff.setCheckLists(checkLists);
        staffRepository.save(staff);
    }

    private CheckList getCheckListEntity(CheckListRequestDto checkListRequestDto) {
        return checkListMapper.toCheckListEntity(checkListRequestDto);
    }

    private List<CheckList> getCheckListsByStaffId(Long staffId) {
        return checkListRepository.findCheckListsByStaffId(staffId);
    }

    public List<CheckList> getCompleteCheckListsByCategory(Long storeId, CategoryRequestDto categoryRequestDto) {
        List<Staff> staffs = storeService.getStaffsByStoreId(storeId);
        List<CheckList> checkLists = new ArrayList<>();
        for (Staff staff : staffs) {
            if (!staff.getCheckLists().isEmpty()) {
                for (CheckList checkList : staff.getCheckLists()) {
                    if (checkList.isComplete() && equalCategory(checkList.getId(), categoryRequestDto.getId()) != null) {
                        checkLists.add(checkList);
                    }
                }
            }
        }
        return checkLists;
    }

    public List<CheckList> getUnCompletedCheckListsByCategory(Long storeId, CategoryRequestDto categoryRequestDto) {
        List<Staff> staffs = storeService.getStaffsByStoreId(storeId);
        List<CheckList> checkLists = new ArrayList<>();
        for (Staff staff : staffs) {
            if (!staff.getCheckLists().isEmpty()) {
                for (CheckList checkList : staff.getCheckLists()) {
                    if (!checkList.isComplete() && equalCategory(checkList.getId(), categoryRequestDto.getId()) != null) {
                        checkLists.add(checkList);
                    }
                }
            }
        }
        return checkLists;
    }

    private CheckList equalCategory(Long id, Long categoryId) {
        return checkListRepository.findCheckListByCategoryIdAndId(id, categoryId);
    }

    public CheckList getCheckList(Long checklistId) {
        return checkListRepository.findCheckListById(checklistId);
    }

    public void completeCheckList(Long checklistId, Long staffId, MultipartFile[] multipartFile, String memo) throws IOException {
        Staff staff = staffRepository.findById(staffId).orElseThrow();

        List<CheckList> checkLists = getCheckListsByStaffId(staffId);
        CheckList checkList = checkListRepository.findById(checklistId);
        if (Arrays.stream(multipartFile).findAny().isPresent()) {
            for (MultipartFile imageFile : multipartFile) {
                List<Photo> photos = new ArrayList<>();

                Photo photo = photoService.savePhoto(imageFile);
                photos.add(photo);
                checkList.setPhotos(photos);
            }
        }
        checkList.setMemo(memo);
        checkList.setComplete(true);
        checkLists.add(checkList);
        staff.setCheckLists(checkLists);
        staffRepository.save(staff);
    }
}
