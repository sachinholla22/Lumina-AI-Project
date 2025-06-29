import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lumina.ai.Lumina_Ai_Backend.dto.ChatResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.ChatService;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;


@RestController
@RequestMapping("/api")
public class ChatController{

     private final ChatService chatService;
     private final JwtUtil jwtUtil;

     public ChatController(ChatService chatService, JwtUtil jwtUtil){
        this.chatService=chatService;
        this.jwtUtil=jwtUtil;
     }
   

    @DeleteMapping("/chats/{chatId}")
    public ResponseEntity<String> deleteChat(
            @PathVariable Long chatId,
            @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.extractUserId(jwt));
        chatService.deleteChat(userId, chatId);
        return ResponseEntity.ok("Chat deleted");
    }

    @GetMapping("/{sessionId}/chats")
    public ResponseEntity<List<ChatResponse>> getChatsBySession(
            @PathVariable Long sessionId,
            @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.extractUserId(jwt));
        return ResponseEntity.ok(chatService.getChatsBySessions(userId, sessionId));
    }

    @PatchMapping("/chats/{chatId}")
    public ResponseEntity<String> updateChat(
        @PathVariable Long chatId,
            @RequestBody UpdateChatRequest request,
            @RequestHeader("Authorization") String authHeader
    ){
String jwt = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.extractUserId(jwt));
        chatService.updateChat(userId, chatId, request.getUserPrompt());
        return ResponseEntity.ok("Chat updated");
    }
}

class UpdateChatRequest {
    private String userPrompt;

    public String getUserPrompt() {
        return userPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }


}