package com.dac.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String token;
    
    @Builder.Default
    private Boolean status = true;  
    
    @Builder.Default
    private String tipoToken = "Bearer";
    
    private String expiraEm;
}