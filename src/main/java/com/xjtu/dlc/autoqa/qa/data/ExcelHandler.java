package com.xjtu.dlc.autoqa.qa.data;

import com.xjtu.dlc.autoqa.qa.entity.QAEntity;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static com.xjtu.dlc.autoqa.config.DataConfig.EXCEL_PATH;

public class ExcelHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExcelHandler.class);


    public static void main(String[] args) throws IOException {
        System.out.println(getAllQAData());
    }


    public static HashMap<String, QAEntity> getAllQAData() {
        logger.info("start get all data done!");
        HashMap<String, QAEntity> rep = new HashMap<>();
        int error = 0;
        File rootFile = new File(EXCEL_PATH);
        String[] extensions = {"xls"};
        Collection<File> files = FileUtils.listFiles(rootFile, extensions, true);
        logger.info("find files：" + files.size());
        for (File file : files) {
            try {
                ArrayList<QAEntity> list = readExcel(file);
                for (QAEntity entity : list) {
                    rep.put(entity.getId(), entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("file：" + file.getAbsolutePath() + " import error.");
                error++;
            }

        }
        logger.info("get all data done and with error number:" + error);
        return rep;

    }

    private static ArrayList<QAEntity> readExcel(File f) {
        ArrayList<QAEntity> list = new ArrayList<>();
        Workbook wb;
        try {
            wb = Workbook.getWorkbook(f);
            Sheet sheet = wb.getSheet(0);
            //遍历
            for (int i = 1; i < sheet.getRows(); i++) {
                QAEntity temp = new QAEntity(
                        Long.valueOf(sheet.getCell(0, i).getContents().trim()),
                        sheet.getCell(1, i).getContents().trim(),
                        sheet.getCell(2, i).getContents().trim(),
                        sheet.getCell(3, i).getContents().trim());

                list.add(temp);
            }
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
