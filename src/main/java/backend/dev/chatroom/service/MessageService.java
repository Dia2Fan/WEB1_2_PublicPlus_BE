package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.Message;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.chatroom.repository.MessageRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public MessageService(MessageRepository messageRepository,
                          ChatRoomRepository chatRoomRepository,
                          ChatParticipantRepository chatParticipantRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
    }

    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO requestDTO) {
        ChatRoom chatRoom = chatRoomRepository.findById(requestDTO.getChatRoomId())
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.CHATROOM_NOT_FOUND));

        ChatParticipant participant = chatParticipantRepository.findById(requestDTO.getParticipantId())
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.PARTICIPANT_NOT_FOUND));

        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setParticipant(participant);
        message.setContent(requestDTO.getContent());
        message.setSentAt(LocalDateTime.now());

        messageRepository.save(message);

        return MessageResponseDTO.fromEntity(message);
    }

    private ChatRoom findChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.CHATROOM_NOT_FOUND));
    }

    private ChatParticipant findParticipant(Long participantId, ChatRoom chatRoom) {
        ChatParticipant participant = chatParticipantRepository.findById(participantId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.PARTICIPANT_NOT_FOUND));
        if (!participant.getChatRoom().equals(chatRoom)) {
            throw new PublicPlusCustomException(ErrorCode.INVALID_CHATROOM);
        }
        return participant;
    }

    private Message createMessage(ChatRoom chatRoom, ChatParticipant participant, String content) {
        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setParticipant(participant);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessagesByChatRoom(Long chatRoomId) {
        return messageRepository.findByChatRoom_ChatRoomId(chatRoomId).stream()
                .map(MessageResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessage(Long chatRoomId, Long messageId, String requesterId) {
        ChatRoom chatRoom = findChatRoom(chatRoomId);
        Message message = findMessage(messageId);
        ChatParticipant requester = findRequester(chatRoom, requesterId);
        validateRequesterPermission(requester, message);
        messageRepository.delete(message);
    }

    private Message findMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.CHAT_NOT_FOUND));
    }

    private ChatParticipant findRequester(ChatRoom chatRoom, String requesterId) {
        return chatParticipantRepository.findByChatRoomAndUserEmail(chatRoom, requesterId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.PARTICIPANT_NOT_FOUND));
    }

    private void validateRequesterPermission(ChatParticipant requester, Message message) {
        System.out.println("Requester isHost: " + requester.isHost());
        System.out.println("Requester User ID: " + (requester.getUser() != null ? requester.getUser().getUserId() : "null"));
        System.out.println("Message User ID: " + (message.getParticipant().getUser() != null ? message.getParticipant().getUser().getUserId() : "null"));

        if (requester.getUser() != null && message.getParticipant().getUser() != null) {
            if (!message.getParticipant().getUser().getUserId().equals(requester.getUser().getUserId())
                    && !requester.isHost()) {
                throw new PublicPlusCustomException(ErrorCode.CHAT_NOT_DELETE);
            }
        } else {
            throw new PublicPlusCustomException(ErrorCode.NOT_USER_INFORMATION);
        }
    }
}
