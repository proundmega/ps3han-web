package org.proundmega.ps3xploidtools.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RapSigningDTO {
    private MultipartFile actDat;
    private MultipartFile idspHex;
    private MultipartFile database;

}
