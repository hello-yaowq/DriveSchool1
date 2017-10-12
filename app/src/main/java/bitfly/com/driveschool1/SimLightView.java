package bitfly.com.driveschool1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.HashMap;


import static android.media.CamcorderProfile.get;
import static bitfly.com.driveschool1.ConstantUtil.*;

/**
 * Created by ll on 2017/10/10.
 */

public class SimLightView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    private ActionThread actionThread;
    DriveSchoolActivity driveSchoolActivity;

    SoundPool soundPool;
    HashMap<Integer,Integer> soundPoolMap;
    Bitmap backgroundBmp;
    Bitmap turnLeftOnBmp;
    Bitmap turnLeftOffBmp;
    Bitmap turnRightOnBmp;
    Bitmap turnRightOffBmp;
    Bitmap turnFarOnBmp;
    Bitmap turnFarOffBmp;
    Bitmap turnFlashOnBmp;
    Bitmap turnFlashOffBmp;
    Bitmap turnOffLightBmp;
    Bitmap widthLightBmp;
    Bitmap nearLightOnBmp;
    Bitmap urgentOnBmp;
    Bitmap urgentOffBmp;

//    int lightSwitchStatus=0;
//    int controllightStatus =0;
//    boolean urgentLightStatus=false;
    int lightStatus=0;
    ArrayList<Integer> actionList = new ArrayList<>();
    ArrayList<Integer> randomAction = new ArrayList<>();

    int testAction=0;

    RectF turnOfflightRect = new RectF(126.0f, 16.0f, 170.0f, 60.0f);
    RectF widthLightRect = new RectF(210.0f, 60.0f, 250.0f, 100.0f);
    RectF nearLightRect = new RectF(230.0f, 130.0f, 270.0f, 160.0f);
    RectF turnRightRect = new RectF(430.0f, 270.0f, 500.0f, 340.0f);
    RectF turnLeftRect = new RectF(430.0f, 500.0f, 500.0f, 570.0f);
    RectF turnFarRect = new RectF(320.0f, 380.0f, 390.0f, 450.0f);
    RectF turnFlashRect = new RectF(530.0f, 380.0f, 6000.0f, 450.0f);
    RectF urgentRect = new RectF(1000.0f, 16.0f, 1124.0f, 140.0f);

    public SimLightView(DriveSchoolActivity driveSchoolActivity) {
        super(driveSchoolActivity);
        this.getHolder().addCallback(this);
        this.driveSchoolActivity = driveSchoolActivity;
        initBitmap();
        initSounds();
        initExam();
        lightStatus = 0;
    }

    public void initSounds(){
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(0, soundPool.load(getContext(), R.raw.startexam, 1));
        soundPoolMap.put(1, soundPool.load(getContext(), R.raw.checklight, 1));
        soundPoolMap.put(2, soundPool.load(getContext(), R.raw.turnonlight, 1));
        soundPoolMap.put(3, soundPool.load(getContext(), R.raw.turnofflight, 1));
        soundPoolMap.put(4, soundPool.load(getContext(), R.raw.continueexam, 1));
        soundPoolMap.put(5, soundPool.load(getContext(), R.raw.turnleft, 1));
        soundPoolMap.put(6, soundPool.load(getContext(), R.raw.turnright, 1));
        soundPoolMap.put(7, soundPool.load(getContext(), R.raw.crossing, 1));
        soundPoolMap.put(8, soundPool.load(getContext(), R.raw.overtaking, 1));
        soundPoolMap.put(9, soundPool.load(getContext(), R.raw.pedestraincrossing, 1));
        soundPoolMap.put(10, soundPool.load(getContext(), R.raw.roadsideparking, 1));
        soundPoolMap.put(11, soundPool.load(getContext(), R.raw.defectiveilumination, 1));
        soundPoolMap.put(12, soundPool.load(getContext(), R.raw.bridgesharpbend, 1));
        soundPoolMap.put(13, soundPool.load(getContext(), R.raw.gostraight, 1));
    }

    public void playSound(int sound, int loop){
        AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent/streamVolumeMax;
        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }

    private void initBitmap() {
        backgroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        turnOffLightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnofflight);
        widthLightBmp = BitmapFactory.decodeResource(getResources(), R.drawable.widthlight);
        nearLightOnBmp = BitmapFactory.decodeResource(getResources(), R.drawable.nearlight);
        turnLeftOffBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnleft1);
        turnLeftOnBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnleft2);
        turnRightOffBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnright1);
        turnRightOnBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnright2);
        turnFarOffBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnfar1);
        turnFarOnBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnfar2);
        turnFlashOffBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnflash1);
        turnFlashOnBmp = BitmapFactory.decodeResource(getResources(), R.drawable.turnflash2);
        urgentOffBmp = BitmapFactory.decodeResource(getResources(), R.drawable.urgentoff);
        urgentOnBmp = BitmapFactory.decodeResource(getResources(), R.drawable.urgenton);
    }

    public void initExam(){
        randomAction.clear();
        for (int i=5; i<=13; i++) {
            randomAction.add(i);
        }
        lightStatus=0;
    }

    public int getRandomAction(){
        if (randomAction.size()==0)
            return -1;
        int index = (int)(Math.random()*randomAction.size());
        int action= randomAction.get(index);
        randomAction.remove(index);
        return action;
    }

    public void resetAction(){
        actionList.clear();
    }

    public boolean checkAction(int action){

        if (action>TEST_ACTION.length)
            return false;

        if (actionList.size()==0){
            actionList.add(lightStatus);
        }

        if (actionList.size() < TEST_ACTION[action].length)
            return false;

        for (int i=0; i<TEST_ACTION[action].length;i++){
            if (TEST_ACTION[action][i] != actionList.get(actionList.size()-TEST_ACTION[action].length+i))
                return false;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (drawThread==null) {
            drawThread = new DrawThread(getHolder(), this);
        }
        drawThread.start();
        if (actionThread==null){
            actionThread = new ActionThread();
        }
        actionThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (drawThread!=null){
            drawThread.setFlag(false);
        }
        if (actionThread!=null){
            actionThread.setFlag(false);
        }
    }

    public void doDraw(Canvas canvas){
        canvas.drawBitmap(backgroundBmp, 0, 0, null);
        int lightSwitchStatus = lightStatus&0x3;
        switch (lightSwitchStatus){
            case 0:
                canvas.drawBitmap(turnOffLightBmp, LIGHT_SWITCH_X,LIGHT_SWITCH_Y, null);
                break;
            case 1:
                canvas.drawBitmap(widthLightBmp, LIGHT_SWITCH_X,LIGHT_SWITCH_Y, null);
                break;
            case 2:
                canvas.drawBitmap(nearLightOnBmp, LIGHT_SWITCH_X,LIGHT_SWITCH_Y, null);
                break;
        }

        int leftRightStatus= (lightStatus&0x30)>>4;
        if (leftRightStatus==1) {
            canvas.drawBitmap(turnLeftOnBmp, TURN_LEFT_X, TURN_LEFT_Y, null);
            canvas.drawBitmap(turnRightOffBmp, TURN_RIGHT_X, TURN_RIGHT_Y, null);
        }else if (leftRightStatus==2) {
            canvas.drawBitmap(turnLeftOffBmp, TURN_LEFT_X, TURN_LEFT_Y, null);
            canvas.drawBitmap(turnRightOnBmp, TURN_RIGHT_X, TURN_RIGHT_Y, null);
        }else {
            canvas.drawBitmap(turnLeftOffBmp, TURN_LEFT_X, TURN_LEFT_Y, null);
            canvas.drawBitmap(turnRightOffBmp, TURN_RIGHT_X, TURN_RIGHT_Y, null);
        }

        int farFlashStatus= (lightStatus&0xc0)>>6;
        if (farFlashStatus==1) {
            canvas.drawBitmap(turnFarOnBmp, TURN_FAR_X, TURN_FAR_Y, null);
            canvas.drawBitmap(turnFlashOffBmp, TURN_FLASH_X, TURN_FLASH_Y, null);
        }else if(farFlashStatus==2) {
            canvas.drawBitmap(turnFarOffBmp, TURN_FAR_X, TURN_FAR_Y, null);
            canvas.drawBitmap(turnFlashOnBmp, TURN_FLASH_X, TURN_FLASH_Y, null);
        }else {
            canvas.drawBitmap(turnFarOffBmp, TURN_FAR_X, TURN_FAR_Y, null);
            canvas.drawBitmap(turnFlashOffBmp, TURN_FLASH_X, TURN_FLASH_Y, null);
        }

        int urgentLightStatus = lightStatus & 0x4;
        if (urgentLightStatus!=0){
            canvas.drawBitmap(urgentOnBmp, URGENT_X, URGENT_Y, null);
        }else {
            canvas.drawBitmap(urgentOffBmp, URGENT_X, URGENT_Y, null);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            if (turnOfflightRect.contains(x,y)){
                lightStatus&=~0x03;
                actionList.add(lightStatus);
            }else if (widthLightRect.contains(x,y)){
                lightStatus&=~0x03;
                lightStatus|=0x01;
                actionList.add(lightStatus);
            }else if (nearLightRect.contains(x,y)){
                lightStatus &= ~0x03;
                lightStatus |= 0x02;
                actionList.add(lightStatus);
            }else if (turnLeftRect.contains(x,y)){
                if ((lightStatus&0x10)==0) {
                    lightStatus&=~0x30;
                    lightStatus|=0x10;
                }else {
                    lightStatus &= ~0x30;
                }
                actionList.add(lightStatus);
            }else if (turnRightRect.contains(x,y)){
                if ((lightStatus&0x20)==0) {
                    lightStatus&=~0x30;
                    lightStatus|=0x20;
                }else {
                    lightStatus &= ~0x30;
                }
                actionList.add(lightStatus);
            }else if (turnFarRect.contains(x,y)){
                if ((lightStatus&0x40)==0) {
                    lightStatus&=~0x40;
                    lightStatus|=0x40;
                }else {
                    lightStatus &= ~0x40;
                }
                actionList.add(lightStatus);
            }else if (turnFlashRect.contains(x,y)){
                if ((lightStatus&0x80)==0) {
                    lightStatus &= ~0x80;
                    lightStatus |= 0x80;
                }else {
                    lightStatus &= ~0x80;
                }
                actionList.add(lightStatus);
            }else if (urgentRect.contains(x,y)){
                if ((lightStatus&0x04)==0){
                    lightStatus&=~0x04;
                    lightStatus|=0x04;
                }else {
                    lightStatus&=~0x04;
                }
                actionList.add(lightStatus);
            }

        }else if (event.getAction() == MotionEvent.ACTION_UP){
            float x = event.getX();
            float y = event.getY();

            if (turnFlashRect.contains(x,y)){
                lightStatus &= ~0xf0;
                actionList.add(lightStatus);
            }
        }
        return true;
    }

    class DrawThread extends Thread{
        boolean flag=true;
        int sleepSpan=10;
        SurfaceHolder surfaceHolder;
        SimLightView simLightView;

        public DrawThread(SurfaceHolder surfaceHolder, SimLightView simLightView) {
            this.surfaceHolder=surfaceHolder;
            this.simLightView = simLightView;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (flag){
                canvas=null;
                try {
                    canvas=this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder){
                        simLightView.doDraw(canvas);
                    }
                } finally {
                    if (canvas!=null){
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

                try {
                    Thread.sleep(sleepSpan);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class ActionThread extends Thread{
        SimLightView simLightView;
        boolean flag=true;
        int testAction;
        int prevAction;

        public ActionThread(SimLightView simLightView) {
            this.simLightView = simLightView;
            testAction=0;
            prevAction=0;
        }

        int delayTime;

        public ActionThread(){
            testAction = START_EXAM;
        }
        public void restartExam(){
            testAction = START_EXAM;
            initExam();
            resetAction();
            delayTime=5;
        }
        @Override
        public void run() {
            while (flag){
                switch (testAction){
                    case START_EXAM:
                        playSound(testAction, 0);
                        testAction = CHECK_LIGHT;
                        delayTime = 5;
                        break;
                    case CHECK_LIGHT:
                        playSound(testAction, 0);
                        resetAction();
                        prevAction = testAction;
                        testAction=TURNON_LIGHT;
                        delayTime=5;
                        break;
                    case TURNOFF_LIGHT:
                        if (!checkAction(prevAction)){
                            restartExam();
                            break;
                        }
                        playSound(testAction, 0);
                        resetAction();
                        prevAction = testAction;
                        testAction=CONTINUE_EXAM;
                        delayTime=5;
                        break;
                    case CONTINUE_EXAM:
                        if (!checkAction(prevAction)){
                            restartExam();
                            break;
                        }
                        playSound(testAction, 0);
                        prevAction = testAction;
                        restartExam();
                        break;
                    case TURNON_LIGHT:
                    case TURN_LEFT:
                    case TURN_RIGHT:
                    case CROSSING:
                    case OVERTAKING:
                    case PEDESTRAIN_CROSSING:
                    case ROADSIDE_PARKING:
                    case DEFECTIVE_ILLUMINATION:
                    case BRIDGE_SHARPBEND:
                    case GO_STRAIGHT:
                        if (!checkAction(prevAction)){
                            restartExam();
                            break;
                        }
                        playSound(testAction, 0);
                        resetAction();
                        prevAction=testAction;
                        if (testAction == ROADSIDE_PARKING || testAction == OVERTAKING) {
                            delayTime = 12;
                        }else{
                            delayTime=7;
                        }
                        testAction = getRandomAction();
                        if (testAction == -1)
                            testAction = TURNOFF_LIGHT;
                        break;
                    default:
                        delayTime=1;
                }

                try {
                    Thread.sleep(delayTime*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
