package com.aapkatrade.buyer.general.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

/**
 * Created by PPC17 on 16-Aug-17.
 */

public class FileUtils {
    static final int READ_BLOCK_SIZE = 100;

    public static void writeStringAsFile(Context context, final String fileContents, String fileName) {

        try {


            FileOutputStream fileout = context.openFileOutput(fileName, context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(fileContents);
            outputWriter.close();

            /*FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(fileContents);
            out.close();*/
        } catch (IOException e) {
            Log.e("Write File Error", e.toString());
        }
    }


    @NonNull
    public static String readFileAsString(Context context, String fileName) {


        String s = "";
        try {
            FileInputStream fileIn = context.openFileInput(fileName);
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();


        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.toString());
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }

        return s;
    }


    public static void updateFileContent(Context context, final String fileContents, String fileName) {

        try {


            FileOutputStream fileout = context.openFileOutput(fileName, context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(fileContents);
            outputWriter.close();


        } catch (IOException e) {
            Log.e("Write File Error", e.toString());
        }
    }


    public static String appendFileContent(Context context, final String fileContents, String fileName) {

        try {


            FileOutputStream fileout = context.openFileOutput(fileName, context.MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(fileContents);
            outputWriter.close();


        } catch (FileNotFoundException e) {

            AndroidUtils.showErrorLog(context,"FileNotFoundException"+e.toString());



            return e.toString();
        } catch (IOException e) {
            AndroidUtils.showErrorLog(context,"IOException"+e.toString());

            return e.toString();

        }
        return SharedPreferenceConstants.SUCESSFULLY_APPEND.toString();
    }

}
