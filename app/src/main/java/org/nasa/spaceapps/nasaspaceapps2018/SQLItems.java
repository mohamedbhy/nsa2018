package org.nasa.spaceapps.nasaspaceapps2018;

public class SQLItems {
    public static final String TABLE_LAUNCHES="LAUNCHES";
    public static final String COLUMN_ID="flight_number";
    public static final String COLUMN_MISSION_NAME="mission_name";
    public static final String COLUMN_LAUNCH_DATE="launch_date";
    public static final String COLUMN_ROCKET_NAME="rocket_name";
    public static final String COLUMN_CORES_NAMES="cores_names";
    public static final String COLUMN_PAYLOADS_IDS="payloads_ids";
    public static final String COLUMN_STATIC_FIRE_DATE="static_fire_date";
    public static final String COLUMN_LAUNCH_SUCCESS="launch_success";
    public static final String COLUMN_DETAILS="details";
    public static final String COLUMN_MORE_DETAILS="more_details";
    public static final String COLUMN_IMAGE="image";
    public static final String COLUMN_YOUTUBE_ID="youtube_id";
    public static final String COLUMN_SAVED="saved";
    public static final String COLUMN_LAUNCH_SITE="launch_site";

    public static final String[] LIST_COLUMNS={COLUMN_ID,COLUMN_MISSION_NAME,COLUMN_LAUNCH_SITE,COLUMN_LAUNCH_DATE,COLUMN_ROCKET_NAME,COLUMN_SAVED};
    public static final String[] DETAILS_COLUMNS={COLUMN_ID,COLUMN_MISSION_NAME,COLUMN_LAUNCH_DATE,COLUMN_ROCKET_NAME,COLUMN_CORES_NAMES,
            COLUMN_PAYLOADS_IDS,COLUMN_STATIC_FIRE_DATE,COLUMN_LAUNCH_SUCCESS,COLUMN_DETAILS,COLUMN_MORE_DETAILS,COLUMN_IMAGE,COLUMN_YOUTUBE_ID,COLUMN_SAVED,COLUMN_LAUNCH_SITE};
    public static final String[] SAVED={COLUMN_ID,COLUMN_SAVED};
    public static final String SQL_CREATE_LAUNCHES=
            "CREATE TABLE "+TABLE_LAUNCHES+"("+
                    COLUMN_ID+" INTEGER PRIMARY KEY,"+
                    COLUMN_MISSION_NAME+" TEXT,"+
                    COLUMN_LAUNCH_SITE+" TEXT,"+
                    COLUMN_LAUNCH_DATE+" DATE,"+
                    COLUMN_ROCKET_NAME+" TEXT,"+
                    COLUMN_CORES_NAMES+" TEXT,"+
                    COLUMN_PAYLOADS_IDS+" TEXT,"+
                    COLUMN_STATIC_FIRE_DATE+" DATE,"+
                    COLUMN_LAUNCH_SUCCESS+" BOOLEAN,"+
                    COLUMN_DETAILS+" TEXT,"+
                    COLUMN_MORE_DETAILS+" TEXT,"+
                    COLUMN_IMAGE+" TEXT,"+
                    COLUMN_YOUTUBE_ID+" TEXT,"+
                    COLUMN_SAVED+" BOOLEAN"+");";

    public static final String SQL_DELETE_LAUNCHES=
            "DROP TABLE "+TABLE_LAUNCHES+" ;";

}
