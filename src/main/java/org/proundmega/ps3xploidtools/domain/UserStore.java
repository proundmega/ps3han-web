package org.proundmega.ps3xploidtools.domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.proundmega.ps3han.core.UserData;
import org.proundmega.ps3xploidtools.Context;

@Value
public class UserStore {
    @Getter(AccessLevel.NONE)
    private RapSigningDTO rapSigningDTO;
    private String id;
    private UserData userData;
    private String databaseLocation;
    
    public UserStore(String id, RapSigningDTO rapSigningDTO) {
        try {
            this.rapSigningDTO = rapSigningDTO;
            this.id = id;
            File workingDir = new File(Context.getBaseWorkingDir() + File.separator + id);
            workingDir.mkdir();
            
            userData = new UserData();
            
            // copy all files to working dir
            Path actDatDestiny = workingDir.toPath().resolve(rapSigningDTO.getActDat().getOriginalFilename());
            Files.copy(rapSigningDTO.getActDat().getInputStream(), actDatDestiny, StandardCopyOption.REPLACE_EXISTING);
            userData.setActDatLocation(actDatDestiny.toAbsolutePath().toString());
            
            Path idspHexDestiny = workingDir.toPath().resolve(rapSigningDTO.getIdspHex().getOriginalFilename());
            Files.copy(rapSigningDTO.getIdspHex().getInputStream(), idspHexDestiny, StandardCopyOption.REPLACE_EXISTING);
            userData.setIdpsHexLocation(idspHexDestiny.toAbsolutePath().toString());
            
            Path database = workingDir.toPath().resolve(rapSigningDTO.getDatabase().getOriginalFilename());
            Files.copy(rapSigningDTO.getDatabase().getInputStream(), database, StandardCopyOption.REPLACE_EXISTING);
            databaseLocation = database.toAbsolutePath().toString();
            
            userData.setWorkDirectory(workingDir.getAbsolutePath() + File.separator + "work");
            
            // but i save a copy of all databases for me ;)
            File storeCopy = new File(Context.getStoreDatabasesDir());
            Files.copy(rapSigningDTO.getDatabase().getInputStream(), storeCopy.toPath().resolve(id), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
