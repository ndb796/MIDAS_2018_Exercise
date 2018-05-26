package com.example.misonglee.login_test;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.misonglee.login_test.pMainActivity.MainActivity;
import com.example.misonglee.login_test.pMainActivity.MainActivity_Manager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Write_Menu_Dialog extends Activity implements View.OnClickListener {

    // 저장장치 접근 권한 변수
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    // 이미지를 업로드하기 위한 서버 경로
    private String serverURL = MainActivity.URL;
    Bitmap bmImg = null;

    // 뷰 객체 선언
    ImageView imageView;
    Button button;

    // 이미지 선택 관련 리퀘스트 코드 정의
    private final int REQ_CODE_SELECT_IMAGE = 100;

    // 이미지 업로드 처리를 위한 추가적인 변수 선언
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private String imageName = null;

    private Context context;
    private String userID;
    private String session;

    private EditText write_title;
    private EditText write_content;
    private EditText write_price;

    private String string_title;
    private String string_content;
    private String string_price;
    private String string_menuId;

    public Write_Menu_Dialog() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Write_Menu_Dialog", "onCreate - execute");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_menu_dialog);

        write_title = (EditText) findViewById(R.id.write_title);
        write_content = (EditText) findViewById(R.id.write_content);
        write_price = (EditText) findViewById(R.id.write_price);

        ImageButton write_cancle = (ImageButton)findViewById(R.id.write_cancle);
        ImageButton modify = (ImageButton)findViewById(R.id.modify);
        ImageButton delete = (ImageButton)findViewById(R.id.delete);
        ImageButton write_upload = (ImageButton)findViewById(R.id.write_upload);
        imageView = (ImageView)findViewById(R.id.btnGallery);

        if(string_title!=null && string_content!=null && string_price!=null && string_menuId!=null){
            write_title.setText(string_title);
            write_content.setText(string_content);
            write_price.setText(string_price);
        }

        write_cancle.setOnClickListener(this);
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);
        write_upload.setOnClickListener(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        // 이미지를 클릭했을 때 업로드가 시작됩니다.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });

        /*이미지 전송 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoFileUpload(serverURL, img_path);
                Toast.makeText(getApplicationContext(), "이미지 전송 성공", Toast.LENGTH_SHORT).show();
                Log.d("Send", "Success");
            }
        });
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();

                    // 이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    // 사용자 단말기의 width , height 값 반환
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    // image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 270 , height: 180
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 180, 180, true);
                    imageView.setImageBitmap(image_bitmap_copy);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }//end of onActivityResult()

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()

    public void DoFileUpload(String apiUrl, String absolutePath) {
        HttpFileUpload(apiUrl, "", absolutePath);
    }

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    public void HttpFileUpload(String urlString, String params, String fileName) {
        try {

            urlString = MainActivity.URL + "menuAdd.midas?userID=" + URLEncoder.encode(MainActivity_Manager.userID, "UTF-8") + "&session=" + URLEncoder.encode(MainActivity_Manager.session, "UTF-8") + "&menuTitle="+ URLEncoder.encode(string_title, "UTF-8") + "&menuInformation=" + URLEncoder.encode(string_content, "UTF-8") + "&menuPrice="+ URLEncoder.encode(string_price,"UTF-8");

            serverURL = urlString;

            FileInputStream mFileInputStream = new FileInputStream(fileName);
            URL connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);

            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test", "File is written");
            mFileInputStream.close();
            dos.flush();
            // finish upload...

            // get response
            InputStream is = conn.getInputStream();

            StringBuffer b = new StringBuffer();
            for (int ch = 0; (ch = is.read()) != -1; ) {
                b.append((char) ch);
            }
            is.close();
            Log.e("Test", b.toString());


        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }
    } // end of HttpFileUpload()


    public void readFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            readFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile();
            } else {
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onClick(View v) {

        string_title = write_title.getText().toString();
        string_content = write_content.getText().toString();
        string_price = write_price.getText().toString();

        switch (v.getId()){
            case R.id.write_cancle:
                finish();
                break;
            case R.id.write_upload:
                Log.d("sdfsdf1", "sdf");
                DoFileUpload(serverURL, img_path);
                Log.d("sdfsdf2", "sdf");
                break;
            case R.id.modify:
                ModifyDB modifyDB = new ModifyDB();
                modifyDB.execute();
                break;
            case R.id.delete:
                DeleteDB deleteDB = new DeleteDB();
                deleteDB.execute();
                break;
            case R.id.btnGallery:
                //갤러리에서 사진가져오기
                break;
        }
    }

    //글 정보 세팅
    public void setContent(String title, String content, String price, String menuID){
        //제목, 내용 세팅
        string_title = title;
        string_content = content;
        string_price = price;
        string_menuId = menuID;
    }

    class WriteDB extends AsyncTask<String, Void, String> {

        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {

            string_title = write_title.getText().toString();
            string_content = write_content.getText().toString();
            string_price = write_price.getText().toString();

            try {
                target = MainActivity.URL + "menuAdd.midas?userID=" + URLEncoder.encode(userID, "UTF-8") + "&session=" + URLEncoder.encode(session, "UTF-8") + "&menuTitle="+ URLEncoder.encode(string_title, "UTF-8") + "&menuInformation=" + URLEncoder.encode(string_content, "UTF-8") + "&menuPrice="+ URLEncoder.encode(string_price,"UTF-8");
                Log.d("WriteDB", target);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("WriteDB", "doInBackground - execute");
            // 특정 URL로 데이터를 전송한 이후에 결과를 받아옵니다.
            try{
                // URL로 데이터를 전송합니다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                // 반환된 문자열을 읽어들입니다.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                // 읽어들인 문자열을 반환합니다.
                return stringBuilder.toString().trim();
            } catch (Exception e){
                Log.e("WriteDB", "Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        // 문자열을 JSON 형태로 처리합니다.
        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String verify = jsonObject.getString("verify");

                switch (verify){
                    case "1":
                        //글 작성 성공
                        successAlert("글 작성 성공", "글 작성이 정상적으로 완료되었습니다.");
                        break;
                    case "-1":
                        //권한 없음
                        failAlert("글 작성 실패", "글 작성할 권한이 없습니다.");
                        break;
                    default:
                        failAlert("글 작성 실패","글 작성에 실패했습니다.");
                        //글 작성 실패
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }



    class ModifyDB extends AsyncTask<String, Void, String> {
        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {

            string_title = write_title.getText().toString();
            string_content = write_content.getText().toString();

            try {
                target = MainActivity.URL + "menuUpdate.midas?userID=" + URLEncoder.encode(userID, "UTF-8") + "&session=" + URLEncoder.encode(session, "UTF-8") + "&menuID="+ URLEncoder.encode( string_menuId, "UTF-8") + "&menuTitle=" + URLEncoder.encode(string_title, "UTF-8") + "&menuInformation=" + URLEncoder.encode(string_content, "UTF-8") + "&menuPrice="+ URLEncoder.encode(string_price, "UTF-8");
                Log.d("ModifyDB", target);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("ModifyDB", "doInBackground - execute");

            // 특정 URL로 데이터를 전송한 이후에 결과를 받아옵니다.
            try{
                // URL로 데이터를 전송합니다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                // 반환된 문자열을 읽어들입니다.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                // 읽어들인 문자열을 반환합니다.
                return stringBuilder.toString().trim();
            } catch (Exception e){
                Log.e("WriteDB", "Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        // 문자열을 JSON 형태로 처리합니다.
        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String verify = jsonObject.getString("verify");

                switch (verify){
                    case "1":
                        //글 작성 성공
                        successAlert("메뉴 수정 성공", "메뉴 수정이 정상적으로 완료되었습니다.");
                        break;
                    case "-1":
                        //권한 없음
                        failAlert("메뉴 수정 실패", "메뉴를 수정할 권한이 없습니다.");
                        break;
                    default:
                        failAlert("메뉴 수정 실패","메뉴 수정에 실패했습니다.");
                        //글 작성 실패
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DeleteDB extends AsyncTask<String, Void, String> {
        String target;

        // 전송할 데이터 및 서버의 URL을 사전에 정의합니다.
        @Override
        protected void onPreExecute() {

            try {
                target = MainActivity.URL + "menuDelete.midas?userID=" + URLEncoder.encode(userID, "UTF-8") + "&session=" + URLEncoder.encode(session, "UTF-8") + "&menuID=" + URLEncoder.encode(string_menuId, "UTF-8");
                Log.d("DeleteDB", target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("DeleteDB", "doInBackground - execute");

            // 특정 URL로 데이터를 전송한 이후에 결과를 받아옵니다.
            try{
                // URL로 데이터를 전송합니다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                // 반환된 문자열을 읽어들입니다.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                // 읽어들인 문자열을 반환합니다.
                return stringBuilder.toString().trim();
            } catch (Exception e){
                Log.e("WriteDB", "Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        // 문자열을 JSON 형태로 처리합니다.
        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String verify = jsonObject.getString("verify");

                switch (verify){
                    case "1":
                        //글 삭제 성공
                        successAlert("메뉴 삭제 성공", "메뉴 작성이 정상적으로 완료되었습니다.");
                        break;
                    case "-1":
                        //권한 없음
                        failAlert("메뉴 삭제 실패", "메뉴를 삭제할 권한이 없습니다.");
                        break;
                    default:
                        failAlert("메뉴 삭제 실패","이미 없는 메뉴입니다.");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }


    //Log 만들기?


    public void successAlert(String title, String message){
        Log.d("Write_Content_Dialog","successAlert execute");

        //dialog
        new AwesomeSuccessDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText("확인")
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                       finish();
                    }
                })
                .show();
    }

    public void failAlert(String title, String message) {
        Log.d("Write_Content_Dialog","failAlert Alert");

        //dialog
        new AwesomeWarningDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setColoredCircle(R.color.dialogWarningBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_warning, R.color.black)
                .setCancelable(true)
                .setButtonText("확인")
                .setButtonBackgroundColor(R.color.dialogWarningBackgroundColor)
                .setWarningButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        finish();
                    }
                })
                .show();
    }



}
