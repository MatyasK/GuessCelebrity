package com.example.ap.tdi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> Urls= new ArrayList<>();
    public ArrayList<String> Names = new ArrayList<>();
    public int choosenCeleb = 0;
    public ImageView pictureView;
    public Button button1, button2, button3, button4;


    public class DownloadTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                //urlConnection.connect();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){

                    char current = (char) data;

                    result += current;


                    data = reader.read();


                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class ImageDownload extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);

        pictureView = (ImageView)findViewById(R.id.celebrityImgView);


        DownloadTask task = new DownloadTask();
        String result =  null;
        try {

            result = task.execute("http://www.posh24.com/celebrities").get();

            String[] splitResult = result.split("<div class=\"sidebarInnerContainer\">");




            Pattern p = Pattern.compile("img src=\"(.*?)\"");

            Matcher m = p.matcher(splitResult[0]);

            while (m.find()){
               // System.out.println("the result:" + m.group(1));
                Urls.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");

            m = p.matcher(splitResult[0]);
            while (m.find()){
               // System.out.println(m.group(1));
                Names.add(m.group(1));
            }


//            Random random = new Random();
//            choosenCeleb = random.nextInt(Urls.size());
//
//
//            ImageDownload imageDownload = new ImageDownload();



           // Log.i("contentof result: ","just "+ result);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        nwQuiz();
    }
    public void nwQuiz(){
        Random random = new Random();
        choosenCeleb = random.nextInt(Urls.size());
        String choosenCelebname = Names.get(choosenCeleb);
        int randomButton = random.nextInt(4);
        System.out.println("Position " + randomButton);
        if (randomButton == 1){
            button1.setText(choosenCelebname);
            button2.setText(Names.get(random.nextInt(Names.size())));
            button3.setText(Names.get(random.nextInt(Names.size())));
            button4.setText(Names.get(random.nextInt(Names.size())));
        }else if (randomButton == 2){
            button1.setText(Names.get(random.nextInt(Names.size())));
            button2.setText(choosenCelebname);
            button3.setText(Names.get(random.nextInt(Names.size())));
            button4.setText(Names.get(random.nextInt(Names.size())));
        }else if (randomButton == 3){
            button1.setText(Names.get(random.nextInt(Names.size())));
            button2.setText(Names.get(random.nextInt(Names.size())));
            button3.setText(choosenCelebname);
            button4.setText(Names.get(random.nextInt(Names.size())));
        }else {
            button1.setText(Names.get(random.nextInt(Names.size())));
            button2.setText(Names.get(random.nextInt(Names.size())));
            button4.setText(Names.get(random.nextInt(Names.size())));
            button3.setText(choosenCelebname);
        }
        ImageDownload imageDownload = new ImageDownload();

        try {
            Bitmap picture = imageDownload.execute(Urls.get(choosenCeleb)).get();
            pictureView.setImageBitmap(picture);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
