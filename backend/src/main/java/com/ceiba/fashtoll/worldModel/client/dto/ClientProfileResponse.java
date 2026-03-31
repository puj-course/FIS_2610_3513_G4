package com.ceiba.fashtoll.worldModel.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientProfileResponse {
    private String name;
    private String email;
}
