package org.genericdaw.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int PICK_AUDIO = 1001;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private MediaPlayer player;
    private TimelineView timeline;
    private TextView status;
    private TextView tempoText;
    private int tempo = 120;

    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            if(player!=null){
                int d=player.getDuration();
                int p=player.getCurrentPosition();
                if(d>0) timeline.setProgress((float)p/(float)d);
                status.setText(formatTime(p)+" / "+formatTime(d));
            }
            handler.postDelayed(this,50);
        }
    };

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.BLACK);

        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xff0b0d10);
        root.setPadding(14,10,14,10);

        TextView title=new TextView(this);
        title.setText("GENERIC DAW  •  ANDROID BASIC");
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        title.setPadding(4,4,4,8);
        root.addView(title,new LinearLayout.LayoutParams(-1,-2));

        LinearLayout transport=new LinearLayout(this);
        transport.setGravity(Gravity.CENTER_VERTICAL);

        Button open=button("IMPORT AUDIO");
        Button play=button("PLAY");
        Button pause=button("PAUSE");
        Button stop=button("STOP");
        Button minus=button("BPM −");
        Button plus=button("BPM +");
        tempoText=new TextView(this);
        tempoText.setTextColor(Color.WHITE);
        tempoText.setTextSize(16);
        tempoText.setGravity(Gravity.CENTER);
        updateTempo();

        transport.addView(open);
        transport.addView(play);
        transport.addView(pause);
        transport.addView(stop);
        transport.addView(minus);
        transport.addView(tempoText,new LinearLayout.LayoutParams(100,-2));
        transport.addView(plus);
        root.addView(transport,new LinearLayout.LayoutParams(-1,-2));

        timeline=new TimelineView(this);
        root.addView(timeline,new LinearLayout.LayoutParams(-1,0,1f));

        status=new TextView(this);
        status.setText("Import an audio file to start");
        status.setTextColor(0xffc7ccd6);
        status.setTextSize(14);
        status.setPadding(4,8,4,4);
        root.addView(status,new LinearLayout.LayoutParams(-1,-2));

        setContentView(root);

        open.setOnClickListener(v->openAudio());
        play.setOnClickListener(v->{ if(player!=null) player.start(); });
        pause.setOnClickListener(v->{ if(player!=null && player.isPlaying()) player.pause(); });
        stop.setOnClickListener(v->{
            if(player!=null){ player.pause(); player.seekTo(0); timeline.setProgress(0f); }
        });
        minus.setOnClickListener(v->{ tempo=Math.max(40,tempo-1); updateTempo(); });
        plus.setOnClickListener(v->{ tempo=Math.min(300,tempo+1); updateTempo(); });
        timeline.setOnSeekFractionListener(f->{
            if(player!=null && player.getDuration()>0) player.seekTo((int)(f*player.getDuration()));
        });

        handler.post(ticker);
    }

    private Button button(String text){
        Button b=new Button(this);
        b.setText(text);
        b.setAllCaps(false);
        return b;
    }

    private void updateTempo(){ tempoText.setText(tempo+" BPM"); }

    private void openAudio(){
        Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("audio/*");
        startActivityForResult(i,PICK_AUDIO);
    }

    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode!=PICK_AUDIO || resultCode!=RESULT_OK || data==null) return;
        Uri uri=data.getData();
        if(uri==null) return;
        try{
            getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }catch(Exception ignored){}
        load(uri);
    }

    private void load(Uri uri){
        releasePlayer();
        try{
            player=new MediaPlayer();
            player.setDataSource(this,uri);
            player.setOnPreparedListener(mp->{
                timeline.setHasClip(true);
                timeline.setProgress(0f);
                status.setText(fileName(uri)+"  •  "+formatTime(mp.getDuration()));
            });
            player.setOnCompletionListener(mp->timeline.setProgress(1f));
            player.prepareAsync();
            status.setText("Loading…");
        }catch(Exception e){
            status.setText("Could not load audio: "+e.getMessage());
        }
    }

    private String fileName(Uri uri){
        String name="Audio";
        try(android.database.Cursor c=getContentResolver().query(uri,null,null,null,null)){
            if(c!=null && c.moveToFirst()){
                int idx=c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if(idx>=0) name=c.getString(idx);
            }
        }catch(Exception ignored){}
        return name;
    }

    private String formatTime(int ms){
        if(ms<0) ms=0;
        int total=ms/1000;
        return String.format(java.util.Locale.US,"%02d:%02d",total/60,total%60);
    }

    private void releasePlayer(){
        if(player!=null){ try{player.release();}catch(Exception ignored){} player=null; }
    }

    @Override protected void onDestroy(){
        handler.removeCallbacks(ticker);
        releasePlayer();
        super.onDestroy();
    }
}
