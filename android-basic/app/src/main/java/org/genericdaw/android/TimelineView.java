package org.genericdaw.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class TimelineView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float progress = 0f;
    private boolean hasClip = false;
    private OnSeekFractionListener seekListener;

    public interface OnSeekFractionListener { void onSeek(float fraction); }

    public TimelineView(Context context) { super(context); setBackgroundColor(0xff101216); }

    public void setHasClip(boolean value) { hasClip = value; invalidate(); }
    public void setProgress(float value) { progress = Math.max(0f, Math.min(1f, value)); invalidate(); }
    public void setOnSeekFractionListener(OnSeekFractionListener listener) { seekListener = listener; }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w=getWidth(), h=getHeight();
        int tracks=8;
        float lane=h/tracks;
        paint.setStrokeWidth(1f);
        for(int i=0;i<=tracks;i++){
            paint.setColor(0xff2a2e35);
            canvas.drawLine(0,i*lane,w,i*lane,paint);
        }
        for(int i=0;i<=16;i++){
            paint.setColor(i%4==0?0xff3f4652:0xff252a31);
            canvas.drawLine(i*w/16f,0,i*w/16f,h,paint);
        }
        if(hasClip){
            paint.setColor(0xff5c7cfa);
            canvas.drawRoundRect(new RectF(w*.05f,lane*.18f,w*.95f,lane*.82f),10,10,paint);
            paint.setColor(0xffffffff);
            paint.setTextSize(Math.max(18f,lane*.28f));
            canvas.drawText("AUDIO CLIP",w*.07f,lane*.58f,paint);
        }
        paint.setColor(0xffffb000);
        paint.setStrokeWidth(4f);
        float x=progress*w;
        canvas.drawLine(x,0,x,h,paint);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()==MotionEvent.ACTION_MOVE){
            float f=Math.max(0f,Math.min(1f,event.getX()/Math.max(1f,getWidth())));
            setProgress(f);
            if(seekListener!=null) seekListener.onSeek(f);
            return true;
        }
        return true;
    }
}
