package backend.dev.facility.service;

import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.testdata.FacilityInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(FacilityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
public class FacilitySearchServiceTests {

    @Autowired
    private FacilityDetailsRepository facilityDetailsRepository; // FacilityRepository를 주입받아 데이터를 검색하거나 검증할 수 있음

    @Autowired
    private FacilityDetailService facilityService;

    @BeforeEach
    public void setUp() {
    }
    @Test
    public void testGetFacilityById_Success() {
        // given
        String id = "FAC1";
        FacilityDetails facility = facilityDetailsRepository.findById(id).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.FACILITY_NOT_FOUND));

        // when
        FacilityDetailsResponseDTO response = facilityService.getFacilityDetails(id);

        // then
        assertNotNull(response);
        assertEquals(facility.getFacilityName(), response.getFacilityName());
    }



}
