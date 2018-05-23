package org.proundmega.ps3xploidtools.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.proundmega.ps3han.core.PSNStore;
import org.proundmega.ps3xploidtools.Context;
import org.proundmega.ps3xploidtools.domain.RapSigningDTO;
import org.proundmega.ps3xploidtools.domain.UserStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String getMainView() {
        return "index.html";
    }

    @GetMapping("/signing/rap")
    public String getViewSigningRap(Model model) {
        model.addAttribute("rapSigningDTO", new RapSigningDTO());
        return "rapSigning.html";
    }

    @PostMapping("/signing/rap")
    public ResponseEntity<InputStreamResource> getDownloadFile(@ModelAttribute RapSigningDTO rapSigningDTO) {
        try {
            String id = RequestContextHolder.currentRequestAttributes().getSessionId();
            
            UserStore userStore = new UserStore(id, rapSigningDTO);
            PSNStore store = new PSNStore(userStore.getDatabaseLocation(), userStore.getUserData(), Context.getBinDir());
            String signedPKGLocation = store.storeRapsAsPkgAndDeleteIntermediateFiles();
            
            File signedPKG = new File(signedPKGLocation);
            
            return makePkdDonwloadable(signedPKG);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ResponseEntity<InputStreamResource> makePkdDonwloadable(File fileToSend) throws FileNotFoundException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileToSend));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + fileToSend.getName())
                .contentLength(fileToSend.length())
                .body(resource);
    }
}
