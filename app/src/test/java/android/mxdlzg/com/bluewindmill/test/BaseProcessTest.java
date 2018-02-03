package android.mxdlzg.com.bluewindmill.test;

import android.mxdlzg.com.bluewindmill.model.entity.NetResult;
import android.mxdlzg.com.bluewindmill.model.process.BaseProcess;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 廷江 on 2017/12/24.
 */
public class BaseProcessTest {
    @Test
    public void process() throws Exception {
        InputStream inputStream = new FileInputStream("E:\\AndriodDevelop\\AndroidStudioProjects\\BlueWindmill\\app\\src\\test\\java\\android\\mxdlzg\\com\\bluewindmill\\test\\Untitled-6.html");
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder builder = new StringBuilder();
        String line;
        while ((line=bufferedReader.readLine())!=null){
            builder.append(line);
        }
        bufferedReader.close();reader.close();inputStream.close();
        line = builder.toString();
        NetResult netResult = BaseProcess.process(line,"成绩课程列表");

    }

}