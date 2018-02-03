package android.mxdlzg.com.bluewindmill.model.process;

import android.mxdlzg.com.bluewindmill.model.config.Config;
import android.mxdlzg.com.bluewindmill.model.entity.Cell;
import android.mxdlzg.com.bluewindmill.model.entity.NetResult;
import android.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.Table;
import android.mxdlzg.com.bluewindmill.model.entity.UnifiedScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class ScoreProcess extends BaseProcess {
    public ScoreProcess(String body) {
        super(body);
    }

    /**
     * EMS Score
     * @param body body
     * @return netresult
     */
    public static NetResult<List<ScoreOBJ>> getScoreTable(String body){
        //Prepare Table
        Table table = BaseProcess.processTable(body,"table[bordercolor=#0000]");

        //New Net Result
        NetResult<List<ScoreOBJ>> result = new NetResult<>(null, Config.NET_RESULT_DEFAULT_ERROR_CODE);

        //If Table
        if (table != null){
            List<ScoreOBJ> list = new ArrayList<>(table.getRowNumber());
            for (int i = 0; i < table.getRowNumber(); i++) {
                list.add(new ScoreOBJ(table.getCell(i,0).getName(),
                        table.getCell(i,1).getName(),
                        table.getCell(i,2).getName(),
                        table.getCell(i,3).getName(),
                        table.getCell(i,4).getName(),
                        table.getCell(i,5).getName(),
                        table.getCell(i,6).getName(),
                        table.getCell(i,7).getName(),
                        table.getCell(i,8).getName()
                        ));
            }
            result.setData(list);
        }

        //Result
        return result;
    }

    /**
     * EMS Score Point
     * @param body body
     * @return point
     */
    public static NetResult<String> getScorePoint(String body) {
        //Talbe
        Table table = BaseProcess.processTable(body,"table[class=list]");

        //Result
        NetResult<String> result = new NetResult<>(null,Config.NET_RESULT_DEFAULT_ERROR_CODE);

        //If Table
        if (table != null){
            result.setData(table.getCell(2,3).getName());
        }

        //Return
        return result;
    }

    /**
     * EMS Unified Score
     * @param body body
     * @return result
     */
    public static NetResult<List<UnifiedScore>> getUnifiedScore(String body) {
        //Talbe
        Table table = BaseProcess.processTable(body,"table[bordercolor=#111111]");

        //Result
        NetResult<List<UnifiedScore>> result = new NetResult<>(null,Config.NET_RESULT_DEFAULT_ERROR_CODE);

        //If Table
        if (table != null){
            List<UnifiedScore> list = new ArrayList<>(table.getRowNumber());
            for (int i = 0; i < table.getRowNumber(); i++) {
                list.add(new UnifiedScore(table.getCell(i,5).getName(),
                        table.getCell(i,6).getName(),
                        table.getCell(i,7).getName(),
                        table.getCell(i,8).getName()
                        ));
            }
            result.setData(list);
        }

        //Return
        return result;
    }
}
