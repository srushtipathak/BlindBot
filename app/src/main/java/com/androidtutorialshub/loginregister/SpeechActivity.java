package com.androidtutorialshub.loginregister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.models.Apps;
import com.androidtutorialshub.loginregister.models.ContactsData;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class SpeechActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextToSpeech mTextToSpeech;
    WebView mainWebView;
    FrameLayout mainAudioLayout;
    MediaPlayer mediaPlayer;
    FloatingActionButton fabAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        requestPermission();
        initViews();

        final Context context = this;
        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    fabAudio.setImageResource(R.drawable.play_white);
                }
                promptSpeechInput();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }
        };
        final GestureDetector detector = new GestureDetector(listener);

        detector.setOnDoubleTapListener(listener);
        detector.setIsLongpressEnabled(true);

        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }

    private void initViews() {
        mediaPlayer = new MediaPlayer();
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        mainWebView = (WebView) findViewById(R.id.webview);
        fabAudio = (FloatingActionButton) findViewById(R.id.audioPlay);

        mainAudioLayout = (FrameLayout) findViewById(R.id.mainAudioLayout);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    fabAudio.setImageResource(R.drawable.play_white);
                }
                promptSpeechInput();
            }
        });
        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub

                mTextToSpeech.speak("Hello! I am there for you what can I do for you?", TextToSpeech.QUEUE_FLUSH, null);

                //mTextToSpeech.speak("Hello! This is neither Siri nor Iris!! Welcome to Swathi's Application!! A Kiddish Application!!" + "Now click on the button ans speak", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {0, 50, 50, 50, 100};

        v.vibrate(pattern, -1);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    doSomeTask(data);
                }

                break;
        }

    }

    private void doSomeTask(Intent data) throws ArrayIndexOutOfBoundsException {
        ArrayList<String> result = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        txtSpeechInput.setText(result.get(0));
        mTextToSpeech.speak(result.get(0).toString(), TextToSpeech.QUEUE_FLUSH, null);

        String[] str = new String[0];
        String[] musicPath = new String[0];

        try {
            str = getMusic();
            musicPath = getMusicPath();
        } catch (Exception ae) {
        }
        String str3[] = result.get(0).split("\\s+");

        String music = str3[0] + " " + str3[1];
        String songs = "";
        String regex = "";
        for (int k = 2; k < str3.length; k++) {
            songs = songs + " " + str3[k];
            regex = regex + "(.*)" + str3[k];
        }
        String musicname = "";
        String musicpath = "";
        for (int j = 0; j < str.length; j++) {
            if (str[j].toLowerCase().matches(regex.toLowerCase() + "(.*)")) {
                musicname = str[j];
                musicpath = musicPath[j];
                Log.d("ParSongs", musicname);
                Log.d("ParSongsPath", musicpath);
                break;
            }
            Log.d("Songs", str[j]);
        }


        if (music.equalsIgnoreCase("Play music") || music.equalsIgnoreCase("Play song")) {

            mainAudioLayout.setVisibility(View.VISIBLE);
            mainWebView.setVisibility(View.INVISIBLE);
            fabAudio.setImageResource(R.drawable.pause_white);
            ImageView closeButton = (ImageView) findViewById(R.id.closeButton);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        fabAudio.setImageResource(R.drawable.play_white);
                    }

                    mainAudioLayout.setVisibility(View.GONE);
                }
            });
            fabAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        fabAudio.setImageResource(R.drawable.play_white);
                    } else {
                        mediaPlayer.start();
                        fabAudio.setImageResource(R.drawable.pause_white);
                    }

                }
            });
            TextView txtTitle = (TextView) findViewById(R.id.audioTitle);
            txtTitle.setText(musicname);
            String filePath = musicpath;
            File f = new File(filePath);
            if (f.exists()) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(filePath);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
            } else {
                mainAudioLayout.setVisibility(View.GONE);
                mainWebView.setVisibility(View.GONE);

                mTextToSpeech.speak("Oops I Could not find this song in you device!", TextToSpeech.QUEUE_FLUSH, null);
            }

        } else if (music.equalsIgnoreCase("Pause music") || music.equalsIgnoreCase("Pause song")) {

        } else if (music.equalsIgnoreCase("Stop music") || music.equalsIgnoreCase("Stop song")) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            } else {
                mTextToSpeech.speak("No song is been played!", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else if (str3[0].equalsIgnoreCase("Open")) {
            searchApp(str3[1]);
        } else if (str3[0].equalsIgnoreCase("Send")) {

            sendMessage(str3, result.get(0));
        } else if (str3[0].equalsIgnoreCase("Call")) {
            callSomeone(str3[1]);
        } else {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            try {
                mainWebView.setVisibility(View.VISIBLE);
                mainAudioLayout.setVisibility(View.GONE);
                mainWebView.setWebViewClient(new myWebClient());

                mainWebView.getSettings().setJavaScriptEnabled(true);
                mainWebView.loadUrl("https://www.google.co.in/search?q=" + URLEncoder.encode(result.get(0), "utf-8"));
                Log.d("QueryMain", "https://www.google.co.in/search?q=" + URLEncoder.encode(result.get(0), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendMessage(String str[], String result) {
        int flag = 0;
        if (str[1].toLowerCase().equalsIgnoreCase("email")) {
            ArrayList<ContactsData> contactsDatas = getNameEmailDetails();
            String lastWord = result.substring(result.lastIndexOf(" ") + 1);

            for (int i = 0; i < contactsDatas.size(); i++) {
                if (contactsDatas.get(i).getName().toLowerCase().contains(lastWord.toLowerCase())) {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setData(Uri.parse(contactsDatas.get(i).getEmailid()));
                    sendIntent.setPackage("com.google.android.gm");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contactsDatas.get(i).getEmailid()});
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, " ");
                    sendIntent.setType("message/rfc822");
                    Matcher m = Pattern.compile(
                            Pattern.quote("email")
                                    + "(.*?)"
                                    + Pattern.quote("to")
                    ).matcher(result);
                    while (m.find()) {
                        String match = m.group(1);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, match);
                    }
                    startActivity(Intent.createChooser(sendIntent, "Share with"));
                    flag = 1;
                    break;
                }

            }
            if (flag == 0) {
                mTextToSpeech.speak("Oops no contact found, Email sending failed.", TextToSpeech.QUEUE_FLUSH, null);
            }

        } else if (str[1].toLowerCase().equalsIgnoreCase("message")) {
            ArrayList<ContactsData> contactsDatas = getNameEmailDetails();
            String lastWord = result.substring(result.lastIndexOf(" ") + 1);

            for (int i = 0; i < contactsDatas.size(); i++) {
                if (contactsDatas.get(i).getName().toLowerCase().contains(lastWord.toLowerCase())) {
                    openWhatsappContact(result);
                    break;

                }

            }


        }
    }

    void openWhatsappContact(String result) {
        Matcher m = Pattern.compile(
                Pattern.quote("message")
                        + "(.*?)"
                        + Pattern.quote("to")
        ).matcher(result);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        while (m.find()) {
            String match = m.group(1);
            i.putExtra(Intent.EXTRA_TEXT, match);
        }
        i.setPackage("com.whatsapp");
        startActivity(i);
    }

    private void searchApp(String app) {
        String packageName = "";
        Dictionary dictionary = new Dictionary();
        ArrayList<Apps> appsArrayList = dictionary.getDictionary();
        for (int i = 0; i < appsArrayList.size(); i++) {
            if (appsArrayList.get(i).getAppName().toLowerCase().contains(app.toLowerCase()))
                packageName = appsArrayList.get(i).getPackageName();
        }


        if (appIsInstalled(packageName) && !packageName.isEmpty()) {
            Intent i = getPackageManager().
                    getLaunchIntentForPackage(packageName);
            startActivity(i);
            mTextToSpeech.speak("Opening " + app, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            mTextToSpeech.speak("Oops no application" + app + " found.", TextToSpeech.QUEUE_FLUSH, null);

        }
    }

    private boolean appIsInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean appInstalled = false;
        try {
            pm.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    private String[] getMusic() {
        final Cursor mCursor = managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();

        String[] songs = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                songs[i] = mCursor.getString(0);
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return songs;
    }

    private String[] getMusicPath() {
        final Cursor mCursor = managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DATA}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();

        String[] songs = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                songs[i] = mCursor.getString(0);
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return songs;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }

    private void callSomeone(String name) {
        ArrayList<ContactsData> contactsDatas = getNameEmailDetails();
        for (int i = 0; i < contactsDatas.size(); i++) {
            if (contactsDatas.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mTextToSpeech.speak("Calling " + name, TextToSpeech.QUEUE_FLUSH, null);
                startActivity(new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + contactsDatas.get(i).getPhone())));
                break;
            }
            mTextToSpeech.speak("Oops no contact found, Phone call failed.", TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    /**********
     * 1. Open Apps,Send Messages & Email
     * 2. Well formed Comments.
     * 3. Full Screen access of MainScreen via double Tap on activity which enables the Google Voice Dialog.
     * 4. WebAccessfor Visual Impact People  -
     */

    public ArrayList<ContactsData> getNameEmailDetails() {
        ArrayList<ContactsData> names = new ArrayList<ContactsData>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if (email != null) {
                        names.add(new ContactsData(name, phone, email));
                    }
                }
                cur1.close();
            }
        }
        return names;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS}, 1001);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SpeechActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access all the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS},
                                                        1001);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
                break;
        }
    }
}
