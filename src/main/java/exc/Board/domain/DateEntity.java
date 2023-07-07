package exc.Board.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
public class DateEntity {
    private String createById;
    private LocalDateTime createdDate;
    private String modifiedById;
    private LocalDateTime modifiedDate;

    public DateEntity(String createById, LocalDateTime createdDate, String modifiedById, LocalDateTime modifiedDate) {
        this.createById = createById;
        this.createdDate = createdDate;
        this.modifiedById = modifiedById;
        this.modifiedDate = modifiedDate;
    }

    protected DateEntity() {
    }
}
