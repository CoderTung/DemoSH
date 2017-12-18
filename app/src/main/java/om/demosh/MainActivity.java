package om.demosh;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.dalong.marqueeview.MarqueeView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MarqueeView marqueeView;
    private static int currentVideo = 0;
    private MyVideoView videoview;
    private ArrayList<Uri> mList;
    private Handler mHandler;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoview= (MyVideoView)findViewById(R.id.video_video);

        tvTime = (TextView) findViewById(R.id.tv_time);

        String notice = "人民有信仰，民族有希望，国家有力量";

        marqueeView = (MarqueeView)findViewById(R.id.tv_marquee);
        marqueeView.setFocusable(true);
        marqueeView.requestFocus();
        marqueeView.setText(notice);//设置文本

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 2:
                        tvTime.setText(msg.getData().getString("time"));
                        break;
                }
                return false;
            }
        });

        //请求res下raw目录下的多个视屏循环，自动依次播放
        mList = new ArrayList<Uri>();
        mList.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sh_1));
        mList.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sh_2));
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


        new Thread(new Runnable() {
            @Override
            public void run() {
                /*tvTime.setText(getWebsiteDatetime(webUrl2) + " [百度]" + "/n" + getWebsiteDatetime(webUrl3) + " [淘宝]" +
                        getWebsiteDatetime(webUrl4) + " [中国科学院国家授时中心]" + "/n" + getWebsiteDatetime(webUrl5) + " [360安全卫士]" +
                        getWebsiteDatetime(webUrl6) + " [beijing-time]");*/
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("time" , getWebsiteDatetime("http://www.baidu.com"));
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }).start();

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

    /**
     * 获取指定网站的日期时间
     *
     * @param webUrl
     * @return
     * @author SHANHY
     * @date   2015年11月27日
     */
    private static String getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd EEEE HH:mm", Locale.CHINA);// 输出北京时间
            Log.e("TTTTT" , sdf.format(date));
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
