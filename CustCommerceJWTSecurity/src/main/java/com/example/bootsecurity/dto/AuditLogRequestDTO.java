package com.example.bootsecurity.dto;

import com.example.bootsecurity.AuditActions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogRequestDTO {
	private Integer userId;
	private Integer accountId;
	private Integer artifactId;
	private String artifactName;
	private AuditActions actionPerformed;

}
