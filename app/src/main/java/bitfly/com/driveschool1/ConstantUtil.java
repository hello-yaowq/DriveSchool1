package bitfly.com.driveschool1;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by ll on 2017/10/10.
 */

public class ConstantUtil {
    public static final int START_EXAM=0;
    public static final int CHECK_LIGHT=1;
    public static final int TURNON_LIGHT=2;
    public static final int TURNOFF_LIGHT=3;
    public static final int CONTINUE_EXAM=4;
    public static final int TURN_LEFT=5;
    public static final int TURN_RIGHT=6;
    public static final int CROSSING=7;
    public static final int OVERTAKING=8;
    public static final int PEDESTRAIN_CROSSING=9;
    public static final int ROADSIDE_PARKING=10;
    public static final int DEFECTIVE_ILLUMINATION=11;
    public static final int BRIDGE_SHARPBEND=12;
    public static final int GO_STRAIGHT=13;


    public static final int LIGHT_SWITCH_X=16;
    public static final int LIGHT_SWITCH_Y=16;

    public static final int TURN_LEFT_X=430;
    public static final int TURN_LEFT_Y=500;
    public static final int TURN_RIGHT_X=430;
    public static final int TURN_RIGHT_Y=270;
    public static final int TURN_FAR_X=320;
    public static final int TURN_FAR_Y=380;
    public static final int TURN_FLASH_X=530;
    public static final int TURN_FLASH_Y=380;
    public static final int URGENT_X=1000;
    public static final int URGENT_Y=16;

    public static final int[][] TEST_ACTION={

            {0},       //            START_EXAM=0;
            {32, 0,128,0, 128, 0},        //            CHECK_LIGHT=1;
            {2},        //            TURNON_LIGHT=2;
            {0},        //            TURNOFF_LIGHT=3;
            {0},        //            CONTINUE_EXAM=4;
            {18},        //            TURN_LEFT=5;
            {34},       //            TURN_RIGHT=6;
            {2},        //            CROSSING=7;
            {18,2,130,2,130,2,34,2},       //            OVERTAKING=8;
            {130,2,130,2},       //            PEDESTRAIN_CROSSING=9;
            {5},       //            ROADSIDE_PARKING=10;
            {66},       //            DEFECTIVE_ILLUMINATION=11;
            {130,2,130,2},        //            BRIDGE_SHARPBEND=12;
            {2}       //            GO_STRAIGHT=13;
    };








}
