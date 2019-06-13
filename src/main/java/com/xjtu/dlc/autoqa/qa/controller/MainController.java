package com.xjtu.dlc.autoqa.qa.controller;


import com.xjtu.dlc.autoqa.qa.answer.CosineSimilarAlgorithmHandler;
import com.xjtu.dlc.autoqa.qa.answer.EsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.xjtu.dlc.autoqa.config.DataConfig.*;

@RestController
public class MainController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/es-index")
    public String index() throws IOException {
        return EsHandler.index();
    }

    @GetMapping("/qa-by-es")
    public String qaByEs(String q, String userID) throws IOException {
        logger.info("q:" + EXCEL_PATH + ", userID:" + userID);
        return EsHandler.search(q);
    }

    @PostMapping("/update-data-by-other-methods")
    public String updateData() throws IOException {
        updateDataMap();
        return "done.";
    }


    @GetMapping("/qa-by-cosine")
    public String qaByCosine(String q, String userID) throws IOException {
        logger.info("q:" + q + ", userID:" + userID);
        return CosineSimilarAlgorithmHandler.answer(q, getExcelDataMap());
    }


}
