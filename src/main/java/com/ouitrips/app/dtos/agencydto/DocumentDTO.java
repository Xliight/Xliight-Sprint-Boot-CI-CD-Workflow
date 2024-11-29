package com.ouitrips.app.dtos.agencydto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Integer id;
    private String title;
    private String type;
    private String filePath;
    private byte[] file;  // Ensure that you only return files if necessary to avoid large payloads.

    // You may include agency details if required
    private Integer agencyId;
}