package com.sstep.demo.staff.service;

import com.sstep.demo.staff.StaffRepository;
import com.sstep.demo.staff.domain.Staff;
import com.sstep.demo.staff.dto.StaffRequestDto;
import com.sstep.demo.staff.dto.StaffResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;

    public StaffResponseDto getStaff(Long staffId) {
        Staff findStaff = getStaffById(staffId);

        return StaffResponseDto.builder()
                .hourMoney(findStaff.getHourMoney())
                .joinStatus(findStaff.isJoinStatus())
                .ownerStatus(findStaff.isOwnerStatus())
                .paymentDate(findStaff.getPaymentDate())
                .startDay(findStaff.getStartDay())
                .submitStatus(findStaff.isSubmitStatus())
                .wageType(findStaff.getWageType())
                .build();
    }
    public void updateStaff(Long staffId, StaffRequestDto staffRequestDto) {
        Staff existingStaff = staffRepository.findById(staffId).orElseThrow();
        existingStaff.setHourMoney(staffRequestDto.getHourMoney());
        existingStaff.setPaymentDate(staffRequestDto.getPaymentDate());
        existingStaff.setStartDay(staffRequestDto.getStartDay());
        existingStaff.setWageType(staffRequestDto.getWageType());

        staffRepository.save(existingStaff);
    }

    public Staff getStaffById(Long staffId) {
        return staffRepository.findById(staffId).orElseThrow();
    }
}
