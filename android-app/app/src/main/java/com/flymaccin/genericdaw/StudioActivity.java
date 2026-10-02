package com.flymaccin.genericdaw;
import android.app.*;import android.os.*;import android.graphics.Color;import android.widget.*;
/** Direct Generic DAW engine harness. Production integration runs inside Demonic AI Studio. */
public class StudioActivity extends Activity{
 public void onCreate(Bundle b){
  super.onCreate(b);
  LinearLayout p=new LinearLayout(this);p.setOrientation(LinearLayout.VERTICAL);p.setPadding(24,24,24,24);p.setBackgroundColor(Color.rgb(9,9,12));
  TextView h=new TextView(this);h.setText("Generic DAW Engine Harness");h.setTextSize(26);h.setTextColor(Color.WHITE);p.addView(h);
  TextView d=new TextView(this);d.setText("ARRANGE · MIXER · PIANO ROLL · AUDIO\n\nDirect test surface only. No launcher behavior. The integrated Generic DAW workspace runs natively inside Demonic AI Studio.");d.setTextColor(Color.LTGRAY);d.setTextSize(15);p.addView(d);
  for(String s:new String[]{"ARRANGE","MIXER","PIANO ROLL","AUDIO"}){Button x=new Button(this);x.setText(s);p.addView(x);}
  setContentView(p);
 }
}