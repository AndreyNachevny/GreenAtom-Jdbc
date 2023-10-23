package rosAtom.jdbc.jdbc.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;

    private String name;

    private Integer age;

    private Double cash;

    private boolean isWoman;
}
