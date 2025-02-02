package backend.dev.facility.service;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.dto.facility.FacilityLocationDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FacilitySearchService {
    private final FacilityDetailsRepository facilityDetailsRepository;

    private final Pageable defaultPageable;

    //카테고리로 시설 찾기
    public Page<FacilityResponseDTO> getFacilitiesByCategory(FacilityCategory facilityCategory) {
        //when 카테고리를 받았을 경우
        try {
            Page<FacilityDetails> byFacilityCategory = facilityDetailsRepository.findByFacilityCategory(facilityCategory, defaultPageable);

            return byFacilityCategory.map(FacilityResponseDTO::fromEntity);
        }catch (Exception e) {
            e.printStackTrace();
            throw new PublicPlusCustomException(ErrorCode.INVALID_CATEGORY);
        }
    }
    //     필터로 시설 찾기
    public Page<FacilityResponseDTO> getFacilitiesByFilter(FacilityFilterDTO facilityFilterDTO,Pageable pageable) {
        try {
            return facilityDetailsRepository.findFacility(facilityFilterDTO,pageable).map(FacilityResponseDTO::fromEntity);
        } catch (Exception e) {
            throw new PublicPlusCustomException(ErrorCode.FACILITY_NOT_FOUND);
        }
    }
    public Page<FacilityResponseDTO> getFacilitiesNearBy(FacilityLocationDTO facilityLocationDTO,Pageable pageable) {
        Page<FacilityDetails> facilitiesByLocation = facilityDetailsRepository.findFacilitiesByLocation(facilityLocationDTO.getLatitude(), facilityLocationDTO.getLongitude(), facilityLocationDTO.getRadius(), pageable);
        return facilitiesByLocation.map(FacilityResponseDTO::fromEntity);
    }
}
