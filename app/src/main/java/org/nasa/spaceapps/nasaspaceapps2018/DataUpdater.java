package org.nasa.spaceapps.nasaspaceapps2018;

import android.content.Context;
import java.util.HashMap;

public class DataUpdater extends DataRetriver {
    public DataUpdater(Context context) {
        super(context);
    }

    @Override
    protected HashMap<String, Boolean> doInBackground(Void... voids) {
        HashMap<String,Boolean> res = new HashMap<>();
        if(isNetworkAvailable() && isSpaceXAvailable()){
            try {
                updateDBData();
            } catch (Exception e) {
                res.put("UpdateSuccess",false);
                e.printStackTrace();
            }
        }else res.put("NetworkErr",true);
        return res;
    }
}
