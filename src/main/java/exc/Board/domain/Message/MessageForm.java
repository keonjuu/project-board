package exc.Board.domain.Message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageForm {

    String msg = "";
    String href ="";

    public MessageForm(String msg, String href) {
        this.msg = msg;
        this.href = href;
    }
}
