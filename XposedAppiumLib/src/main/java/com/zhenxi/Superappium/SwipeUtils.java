package com.zhenxi.Superappium;


import static android.view.MotionEvent.TOOL_TYPE_FINGER;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;


/**
 * �¼�������װ
 */
public class SwipeUtils {

    public static final int HIGH = 10;
    public static final int NORMAL = 100;
    public static final int LOW = 1000;

    private static final long DEFAULT_DURATION = 1500;


    /**
     * Ĭ�϶Ե�ǰ��Ļ���л���
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public static void simulateScroll(int startX, int startY, int endX, int endY) {
        Activity topActivity = PageManager.getTopActivity();
        if (topActivity == null) {

            return;
        }
        simulateScroll(new ViewImage(topActivity.getWindow().getDecorView()),
                startX, startY, endX, endY, DEFAULT_DURATION);

    }


    /**
     * ��Զ�Ӧ��View,ģ�����ƻ���
     *
     * @param view   ������ view
     * @param startX ��ʼλ�� x
     * @param startY ��ʼλ�� y
     * @param endX   �յ�λ�� x
     * @param endY   �յ�λ�� y
     */
    public static void simulateScroll(ViewImage view, int startX, int startY, int endX, int endY) {
        simulateScroll(view, startX, startY, endX, endY, DEFAULT_DURATION);
    }


    /**
     * ģ�����ƻ���
     *
     * @param view     ������ view
     * @param startX   ��ʼλ�� x
     * @param startY   ��ʼλ�� y
     * @param endX     �յ�λ�� x
     * @param endY     �յ�λ�� y
     * @param duration ����ʱ�� ��λ��ms
     */
    public static void simulateScroll(ViewImage view, int startX, int startY, int endX, int endY, long duration) {
        simulateScroll(view, startX, startY, endX, endY, duration, NORMAL);
    }


    /**
     * ģ�����ƻ���
     *
     * @param view     ������ view
     * @param startX   ��ʼλ�� x
     * @param startY   ��ʼλ�� y
     * @param endX     �յ�λ�� x
     * @param endY     �յ�λ�� y
     * @param duration ����ʱ�� ��λ��ms
     * @param period   ��������
     *                 {@link #LOW} ��
     *                 {@link #NORMAL} ����
     *                 {@link #HIGH} ��
     */
    public static void simulateScroll(ViewImage view, int startX, int startY, int endX, int endY, long duration, int period) {
        dealSimulateScroll(view, startX, startY, endX, endY, duration, period);
    }

    /**
     * ģ�����ƻ���
     *
     * @param activity ��ǰ�� activity
     * @param startX   ��ʼλ�� x
     * @param startY   ��ʼλ�� y
     * @param endX     �յ�λ�� x
     * @param endY     �յ�λ�� y
     * @param duration ����ʱ�� ��λ ms
     * @param period   ��������
     *                 {@link #LOW} ��
     *                 <p>
     *                 {@link #NORMAL} ����
     *                 {@link #HIGH} ��
     */
    public static void simulateScroll(ViewImage activity, float startX, float startY, float endX, float endY, long duration, int period) {
        dealSimulateScroll(activity, startX, startY, endX, endY, duration, period);
    }

    private static void dealSimulateScroll(ViewImage object, float startX, float startY, float endX, float endY, long duration, int period) {
        long downTime = SystemClock.uptimeMillis();
        Handler handler = new ViewHandler(object);
        object.getOriginView().dispatchTouchEvent(
                createFingerMotionEvent(object.getOriginView(), downTime, downTime,
                        MotionEvent.ACTION_DOWN, startX, startY, 0));
        GestureBean bean = new GestureBean(startX, startY, endX, endY, duration, period);
        Message.obtain(handler, 1, bean).sendToTarget();
    }


    static class ViewHandler extends Handler {
        WeakReference<ViewImage> mView;

        ViewHandler(ViewImage activity) {
            super(Looper.getMainLooper());
            mView = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ViewImage theView = mView.get();
            if (theView == null) {
                return;
            }
            long downTime = SystemClock.uptimeMillis();
            GestureBean bean = (GestureBean) msg.obj;
            long count = bean.count;
            if (count >= bean.totalCount) {
                theView.getOriginView().dispatchTouchEvent(createFingerMotionEvent(theView.getOriginView(),
                        downTime, downTime, MotionEvent.ACTION_UP, bean.endX, bean.endY, 0));
            } else {
                theView.getOriginView().dispatchTouchEvent(createFingerMotionEvent(theView.getOriginView(),
                        downTime, downTime, MotionEvent.ACTION_MOVE, bean.startX + bean.ratioX * count,
                        bean.startY + bean.ratioY * count, 0));
                bean.count++;
                Message message = new Message();
                message.obj = bean;
                sendMessageDelayed(message, bean.period);
            }
        }
    }

    static class GestureBean {

        /**
         * ��ʼλ�� X
         */
        float startX;
        /**
         * ��ʼλ�� Y
         */
        float startY;
        /**
         * �յ�λ�� X
         */
        float endX;
        /**
         * �յ�λ�� Y
         */
        float endY;
        /**
         * ÿ������ x �ƶ���λ��
         */
        float ratioX;
        /**
         * ÿ������ y �ƶ���λ��
         */
        float ratioY;
        /**
         * �ܹ�����
         */
        long totalCount;
        /**
         * ��ǰ����
         */
        long count = 0;

        int period = NORMAL;

        GestureBean(float startX, float startY, float endX, float endY, long duration, int speed) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.period = speed;
            totalCount = duration / speed;
            ratioX = (endX - startX) / totalCount;
            ratioY = (endY - startY) / totalCount;
        }
    }


    /**
     * Create a new MotionEvent, filling in a subset of the basic motion
     * values.  Those not specified here are: device id (always 0), pressure
     * and size (always 1), x and y precision (always 1), and edgeFlags (always 0).
     *
     * @param downTime  The time (in ms) when the user originally pressed down to start
     *                  a stream of position events.  This must be obtained from {@link SystemClock#uptimeMillis()}.
     * @param eventTime The the time (in ms) when this specific event was generated.  This
     *                  must be obtained from {@link SystemClock#uptimeMillis()}.
     * @param action    The kind of action being performed, such as {@link MotionEvent#ACTION_DOWN}.
     * @param x         The X coordinate of this event.
     * @param y         The Y coordinate of this event.
     * @param metaState The state of any meta / modifier keys that were in effect when
     *                  the event was generated.
     */
    private static MotionEvent createFingerMotionEvent(long downTime, long eventTime, int action, float x, float y, int metaState) {
        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        pointerCoords.x = x;
        pointerCoords.y = y;
        MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
        pointerProperties.id = 0;
        pointerProperties.toolType = TOOL_TYPE_FINGER;
        MotionEvent.PointerProperties[] pointerPropertiesArray = new MotionEvent.PointerProperties[]{pointerProperties};
        MotionEvent.PointerCoords[] pointerCoordsArray = new MotionEvent.PointerCoords[]{pointerCoords};
        // @param deviceId The id for the device that this event came from.  An id of zero indicates that the event didn't come from a physical device;
        return MotionEvent.obtain(downTime, eventTime, action, 1, pointerPropertiesArray, pointerCoordsArray, 0, 0, 0, 0, 8, 0, InputDevice.SOURCE_TOUCHSCREEN, 0);
    }

    private static MotionEvent createFingerMotionEvent(View view, long downTime, long eventTime, int action, float x, float y, int metaState) {
        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        pointerCoords.x = x;
        pointerCoords.y = y;
        MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
        pointerProperties.id = view.getId();
        pointerProperties.toolType = TOOL_TYPE_FINGER;
        MotionEvent.PointerProperties[] pointerPropertiesArray = new MotionEvent.PointerProperties[]{pointerProperties};
        MotionEvent.PointerCoords[] pointerCoordsArray = new MotionEvent.PointerCoords[]{pointerCoords};
        // @param deviceId The id for the device that this event came from.  An id of zero indicates that the event didn't come from a physical device;
        return MotionEvent.obtain(downTime, eventTime, action, 1, pointerPropertiesArray, pointerCoordsArray, 0, 0, 0, 0, 8, 0, InputDevice.SOURCE_TOUCHSCREEN, 0);
    }
}
