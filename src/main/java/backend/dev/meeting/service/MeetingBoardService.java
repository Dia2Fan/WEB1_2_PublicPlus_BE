package backend.dev.meeting.service;

import backend.dev.meeting.dto.request.BoardFilterDTO;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.meeting.repository.MeetingBoardRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.Role;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingBoardService {

    private final MeetingBoardRepository meetingBoardRepository;
    private final UserRepository userRepository;

    // Create: 모임 생성
    public MeetingBoardResponseDTO createMeetingBoard(MeetingBoardRequestDTO dto, String requesterId) {

        // 인증되지 않은 사용자 체크
        if (requesterId == null || requesterId.isEmpty()) {
            throw new PublicPlusCustomException(ErrorCode.LOGIN_REQUIRED);
        }
        System.out.println("Id : "+requesterId);
        // 사용자 정보 조회
        User host = userRepository.findById(requesterId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.UNAUTHORIZED_USER));

        // ADMIN 또는 USER 권한 확인
        if (host.getRole() != Role.ADMIN && host.getRole() != Role.USER) {
            throw new PublicPlusCustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        MeetingBoard meetingBoard = new MeetingBoard(dto, host);
        MeetingBoard savedBoard = meetingBoardRepository.save(meetingBoard);

        return new MeetingBoardResponseDTO(savedBoard);
    }
    // Read: 모든 모임 조회
    public List<MeetingBoardResponseDTO> getAllMeetingBoards() {
        List<MeetingBoard> meetingBoards = meetingBoardRepository.findAll();

        // MeetingBoard -> MeetingBoardResponseDTO 변환
        return meetingBoards.stream()
                .map(MeetingBoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Read: 특정 모임 조회
    public MeetingBoardResponseDTO getMeetingBoardById(Long mbId) {
        MeetingBoard meetingBoard = meetingBoardRepository.findById(mbId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.BOARD_NOT_FOUND));

        // MeetingBoard -> MeetingBoardResponseDTO 변환
        return new MeetingBoardResponseDTO(meetingBoard);
    }

    // Update: 모임 수정
    public MeetingBoardResponseDTO updateMeetingBoard(Long mbId, MeetingBoardRequestDTO dto, String requesterId) {
        MeetingBoard meetingBoard = meetingBoardRepository.findById(mbId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.BOARD_NOT_FOUND));
//        !isAdmin(requesterId) &&
        requesterId = SecurityContextHolder.getContext().getAuthentication().getName();
        if ( !isHost(requesterId, meetingBoard.getMbHost().getUserId())) {
            throw new PublicPlusCustomException(ErrorCode.BOARD_NOT_DELETE);
        }

        meetingBoard.setSportType(dto.getSportType());
        meetingBoard.setMbTitle(dto.getMbTitle());
        meetingBoard.setMbContent(dto.getMbContent());
        meetingBoard.setStartTime(dto.getStartTime());
        meetingBoard.setEndTime(dto.getEndTime());
        meetingBoard.setMbLocation(dto.getMbLocation());
        meetingBoard.setMaxParticipants(dto.getMaxParticipants());

        MeetingBoard updated = meetingBoardRepository.save(meetingBoard);
        return new MeetingBoardResponseDTO(updated);
    }

    // Delete: 모임 삭제
    public void deleteMeetingBoard(Long mbId, String requesterId) {
        MeetingBoard meetingBoard = meetingBoardRepository.findById(mbId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.BOARD_NOT_FOUND));

        // ADMIN 또는 HOST 권한 확인
        if (!isAdmin(requesterId) && !isHost(requesterId, meetingBoard.getMbHost().getUserId())) {
            throw new PublicPlusCustomException(ErrorCode.BOARD_NOT_DELETE);
        }

        meetingBoardRepository.deleteById(mbId);
    }

    // 권한 확인 메서드
    private boolean isAdmin(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        return user.getRole() == Role.ADMIN;
    }

    private boolean isHost(String requesterId, String hostId) {
        return requesterId.equals(hostId);
    }

    public Page<MeetingBoardResponseDTO> filterSearch(BoardFilterDTO boardFilterDTO, Pageable pageable) {
        Page<MeetingBoard> meetingBoards = meetingBoardRepository.findMeetingBoards(boardFilterDTO, pageable);
        return meetingBoards.map(MeetingBoardResponseDTO::new);
    }
}