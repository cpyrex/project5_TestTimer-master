package com.murach.ch10_ex5;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

    Timer timer;
    TimerTask task;
    private final String URL_STRING = "http://rss.cnn.com/rss/cnn_tech.rss";
    private final String FILENAME = "news_feed.xml";
;
    private  int i = 0;

    private TextView messageTextView; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        messageTextView = (TextView) findViewById(R.id.messageTextView);


    }
    
    private void startTimer() {
        final long startMillis = System.currentTimeMillis();
        timer = new Timer(true);


        task = new TimerTask() {
            
            @Override
            public void run() {

                long elapsedMillis = System.currentTimeMillis() - startMillis;
                updateView(elapsedMillis);

                try{
                    // get the URL
                    URL url = new URL(URL_STRING);

                    // get the input stream
                    InputStream in = url.openStream();

                    // get the output stream
                    FileOutputStream out =
                            openFileOutput(FILENAME, Context.MODE_PRIVATE);

                    // read input and write output
                    byte[] buffer = new byte[1024];
                    int bytesRead = in.read(buffer);
                    while (bytesRead != -1)
                    {
                        out.write(buffer, 0, bytesRead);
                        bytesRead = in.read(buffer);
                    }
                    out.close();
                    in.close();
                    i++;
                }
                catch (IOException e) {
                    Log.e("News reader", e.toString());
                }




            }
        };

        timer.schedule(task, 0, 1000);
    }

    private void updateView(final long elapsedMillis) {
        // UI changes need to be run on the UI thread
        messageTextView.post(new Runnable() {

            int elapsedSeconds = (int) elapsedMillis/1000;

            @Override
            public void run() {
               // messageTextView.setText("Seconds: " + elapsedSeconds);
                messageTextView.setText("File downloaded " + elapsedSeconds + " times.");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    public void startButtonClick(View view) {
        startTimer();

    }

    public void stopButtonClick(View view) {

        onPause();
        timer.cancel();
        task.cancel();

    }

}