package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.SendMessageRequest;
import BackEnd.SkillBridge.dto.response.ConversationSummary;
import BackEnd.SkillBridge.dto.response.MessageResponse;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{receiverId}")
    public ResponseEntity<MessageResponse> send(@PathVariable Long receiverId,
                                                @Valid @RequestBody SendMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(currentUser(), receiverId, request));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationSummary>> conversations() {
        return ResponseEntity.ok(messageService.getConversations(currentUser()));
    }

    @GetMapping("/conversations/{partnerId}")
    public ResponseEntity<List<MessageResponse>> conversation(@PathVariable Long partnerId) {
        return ResponseEntity.ok(messageService.getConversation(currentUser(), partnerId));
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
