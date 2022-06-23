package aptech.computer.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialDTO {

    private String accessToken;
    private String refreshToken;


}
