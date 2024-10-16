package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.CsvControllerDocs;
import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.service.CsvService;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class CsvController implements CsvControllerDocs {

    private final CsvService csvService;
    private final VocaService vocaService;
    private final UserService userService;

    public CsvController(CsvService csvService, VocaService vocaService, UserService userService){
        this.csvService = csvService;
        this.vocaService = vocaService;
        this.userService = userService;
    }

    @GetMapping("/vocalist/csvdown/{id}")
    public ResponseEntity<String> csvDown(HttpServletResponse response, @PathVariable("id") Long id) throws IOException {
        VocaListEntity selectedVocaList = vocaService.findVocaListById(id);
        if(selectedVocaList.getSecret() == 2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("판매 중인 단어장은 내보내기 불가.");
        }
        response.setContentType("text/csv; charset=UTF-8");
        String fileName = URLEncoder.encode("단어.csv", "UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");

        try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
             CSVWriter csvWriter = new CSVWriter(writer)) {
            writer.write("\uFEFF");
            csvWriter.writeAll(csvService.listVocaContentString(id));
        }

        return ResponseEntity.status(HttpStatus.OK).body("다운 완료");
    }

    @PostMapping(value = "/readcsv")
    public ResponseEntity<String> readCsv(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody VocaListDto vocaListDto) {
        String filePath = "D:/vocatest/단어.csv";

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            List<String> headerList = new ArrayList<>();
            boolean isFirstLine = true;


            String email = oAuth2User.getAttribute("email");
            VocaListEntity createdVocaListEntity = vocaListDto.toEntity(email);
            vocaService.saveVocaList(createdVocaListEntity);

            UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
            userVocaListEntity.setUserEntity(userService.findUserByEmail(email));
            userVocaListEntity.setVocaListEntity(createdVocaListEntity);
            vocaService.saveUserVocaList(userVocaListEntity);

            while ((line = br.readLine()) != null) {
                List<String> stringList = Arrays.asList(line.split(","));

                if (isFirstLine) {
                    headerList.addAll(stringList);
                    isFirstLine = false;
                } else {

                    VocaContentEntity vocaContentEntity = new VocaContentEntity();
                    vocaContentEntity.setText(stringList.get(0));
                    vocaContentEntity.setTranstext(stringList.get(1));
                    vocaContentEntity.setSampleSentence(stringList.get(2));
                    vocaContentEntity.setVocaListEntity(createdVocaListEntity);

                    vocaService.saveVocaContent(vocaContentEntity);
                    log.info("저장된 단어: {} - {}", stringList.get(0), stringList.get(1));
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("ok");

        } catch (IOException e) {
            log.error("파일 읽기 오류: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 읽기 오류");
        }
    }
}
