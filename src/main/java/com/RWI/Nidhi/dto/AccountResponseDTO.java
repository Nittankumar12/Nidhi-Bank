package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {

	private Integer accpintId;

	private String accountNumber;

	private Status status;

	private String userName;

	private String UserEmail;

}
