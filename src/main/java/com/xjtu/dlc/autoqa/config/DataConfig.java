package com.xjtu.dlc.autoqa.config;

import com.xjtu.dlc.autoqa.qa.data.ExcelHandler;
import com.xjtu.dlc.autoqa.qa.entity.QAEntity;

import java.util.HashMap;

public class DataConfig {

    public static String EXCEL_PATH = "D:\\qa-excel\\";

    private static HashMap<String, QAEntity> DATA_MAP;

    public static HashMap<String, QAEntity> getExcelDataMap() {
        if (DATA_MAP == null) {
            synchronized (DataConfig.class) {
                if (DATA_MAP == null) {
                    DATA_MAP = ExcelHandler.getAllQAData();
                }
            }
        }
        return DATA_MAP;
    }

    public static void updateDataMap() {
        synchronized (DataConfig.class) {
            DATA_MAP = ExcelHandler.getAllQAData();
        }
    }

}
