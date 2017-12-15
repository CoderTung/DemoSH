package om.demosh;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dalong.marqueeview.MarqueeView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MarqueeView marqueeView;
    private static int currentVideo = 0;
    private MyVideoView videoview;
    private ArrayList<Uri> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoview= (MyVideoView)findViewById(R.id.video_video);

        String notice = "人民有信仰，民族有希望，国家有力量";

        marqueeView = (MarqueeView)findViewById(R.id.tv_marquee);
        marqueeView.setFocusable(true);
        marqueeView.requestFocus();
        marqueeView.setText(notice);//设置文本

        //请求res下raw目录下的多个视屏循环，自动依次播放
        mList = new ArrayList<Uri>();
        mList.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.a));
        mList.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.b));
        mList.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.c));
        mList.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.d));
        videoview.setVideoURI(mList.get(currentVideo));
        videoview.requestFocus();
        try {
            videoview.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                nextVideo();
            }

            private void nextVideo() {
                // TODO Auto-generated method stub
                currentVideo++;
                if (currentVideo == mList.size()) {
                    currentVideo = 0;
                }
                videoview.setVideoURI(mList.get(currentVideo));
                videoview.requestFocus();
                videoview.start();
            }
        });


        //自动播放res下的raw目录下的单个视频，循环播放
        // videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.john));
        // videoview.setMediaController(mc);
        // videoview.requestFocus();
        // videoview.start();
        // videoview.setOnCompletionListener(new OnCompletionListener() {
        //
        //	@Override
        //	public void onCompletion(MediaPlayer mp) {
        //	// TODO Auto-generated method stub
        //	videoview.start();
        //	}
        //	});
    }

    @Override
    protected void onStart() {
        super.onStart();
        marqueeView.startScroll();
    }

    @Override
    protected void onStop() {
        super.onStop();
        marqueeView.stopScroll();
    }
}
