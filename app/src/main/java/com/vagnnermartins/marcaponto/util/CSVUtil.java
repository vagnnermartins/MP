package com.vagnnermartins.marcaponto.util;

import android.content.res.Resources;
import android.os.Environment;

import com.vagnnermartins.marcaponto.R;
import com.vagnnermartins.marcaponto.entity.History;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by vagnnermartins on 01/04/15.
 */
public class CSVUtil {

    public static String createCSV(final List<History> histories, final Resources res){
        CSV csv = CSV.separator(';')
                .quote('\'')
                .skipLines(1)
                .charset("UTF-8")
                .create();
        File csvFile = createFile(histories, res);
        csv.write(csvFile, new CSVWriteProc() {
            @Override
            public void process(CSVWriter out) {
                out.writeNext(res.getString(R.string.date),
                        res.getString(R.string.entrance),
                        res.getString(R.string.pause),
                        res.getString(R.string.back),
                        res.getString(R.string.quit),
                        res.getString(R.string.hours),
                        res.getString(R.string.balance));
                for (History history : histories) {
                    Date date = DataUtil.transformStringToDate("dd/MM/yyyy", history.getDay());
                    String formattedDate;
                    if (res.getConfiguration().locale.getLanguage().equals("pt")) {
                        formattedDate = DataUtil.transformDateToSting(date, "dd/MM/yyyy");
                    } else {
                        formattedDate = DataUtil.transformDateToSting(date, "MM/dd/yyyy");
                    }
                    out.writeNext(formattedDate,
                            history.getFormattedEntrance(),
                            history.getFormattedPause(),
                            history.getFormattedBack(),
                            history.getFormattedQuit(),
                            DataUtil.transformSecondsInHourMinutes(history.getTotalDifferencesSecond()));
                }
            }
        });
        return csvFile.getAbsolutePath();
    }

    private static File createFile(List<History> histories, Resources res) {
        File filePath = new File(Environment.getExternalStorageDirectory() + "/" + res.getString(R.string.app_name) + "/");
        File csvFile = new File(filePath, getFileName(histories, res));
        if(!filePath.exists()){
            try {
                filePath.mkdirs();
                if(!csvFile.exists()){
                    csvFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    private static String getFileName(List<History> histories, Resources res) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DataUtil.transformStringToDate("dd/MM/yyyy", histories.get(0).getDay()));
        return "/"+DataUtil.getMonth(calendar.get(Calendar.DAY_OF_MONTH), res) + "_" + calendar.get(Calendar.YEAR) + ".csv";
    }
}
