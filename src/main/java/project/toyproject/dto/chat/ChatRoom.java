package project.toyproject.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

        //일단
        private String roomId;
        private String roomName;

        //생성자 메서드
        public static ChatRoom create(String name) {
                ChatRoom room = new ChatRoom();
                room.roomId = UUID.randomUUID().toString();
                room.roomName = name;
                return room;
        }
}
