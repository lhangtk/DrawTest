package com.example.drawtest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawActivity extends Activity implements View.OnClickListener {
    private DrawView drawView;
    /**
     * 选择图片
     */
    private Button button_choose;
    /**
     * 橡皮擦
     */
    private Button button_eraser;
    /**
     * 画笔
     */
    private Button button_paint;
    /**
     * 激光笔
     */
    private Button button_laser;
    /**
     * 激光笔
     */
    private Button button_laser2;
    /**
     * 保存图片
     */
    private Button button_save;
    private ImageView image;
    private Bitmap bitmap;
    private final static int RESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw);
        button_choose = (Button) findViewById(R.id.button_choose);
        button_eraser = (Button) findViewById(R.id.button_earser);
        button_paint = (Button) findViewById(R.id.button_paint);
        button_laser = (Button) findViewById(R.id.button_laser);
        button_laser2 = (Button) findViewById(R.id.button_laser2);
        button_save = (Button) findViewById(R.id.button_save);
        image = (ImageView) findViewById(R.id.image);
        drawView = (DrawView) findViewById(R.id.draw);
        // 设置监听
        button_choose.setOnClickListener(this);
        button_eraser.setOnClickListener(this);
        button_paint.setOnClickListener(this);
        button_laser.setOnClickListener(this);
        button_laser2.setOnClickListener(this);
        button_save.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri imageFileUri = data.getData();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(imageFileUri), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(bitmap);
            drawView.clear();
        }
    }

    private void GeneratePic() {
        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);

        int w = drawView.getWidth();
        int h = drawView.getHeight();
        // 构建图片
        Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        frame.setDrawingCacheEnabled(true);
        frame.buildDrawingCache();
        bmp = frame.getDrawingCache();

        String savePath = Environment.getExternalStorageDirectory() + "/images";
        String filePath = savePath + "/ScreenShot_1.png";
        try {
            File path = new File(savePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else {//若同名文件已经存在增加文件名后面的序号
                for (int i = 2; ; i++) {
                    filePath = savePath + "/temp_" + i + ".png";
                    file = new File(filePath);
                    if (!file.exists()) {
                        file.createNewFile();
                        break;
                    }
                }
            }
            FileOutputStream fout = new FileOutputStream(file);
            if (fout != null) {
                bmp.compress(CompressFormat.PNG, 90, fout);
                fout.flush();
                fout.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.destroyDrawingCache();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_earser:// 橡皮擦
                drawView.clear();
                break;
            		case R.id.button_paint:// 画笔
			drawView.laser = false;

			break;
            case R.id.button_laser:
                drawView.laser = true;
                break;
        }
    }

//	@Override
//	public void onClick(View v) {// 对多个按钮进行监听
//		switch (v.getId()) {
//		case R.id.button_choose:// 选择图片
//			Intent intent = new Intent(
//					Intent.ACTION_PICK,
//					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//			startActivityForResult(intent, RESULT);
//			break;
//		case R.id.button_earser:// 橡皮擦
//			drawView.setEarser();
//			break;
//		case R.id.button_paint:// 画笔
//			drawView.setPaint(Color.BLUE, 5);
//			break;
//		case R.id.button_laser://激光笔
//			if(button_laser.getText().equals("激光笔")){
//				button_laser.setText("取消激光笔");
//				drawView.setLaser(true);
//			}else if(button_laser.getText().equals("取消激光笔")){
//				button_laser.setText("激光笔");
//				drawView.setLaser(false);
//			}
//			break;
//		case R.id.button_laser2://激光笔
//			if(button_laser2.getText().equals("激光笔2")){
//				button_laser2.setText("取消激光笔2");
//				drawView.setLaser2(true);
//			}else if(button_laser2.getText().equals("取消激光笔2")){
//				button_laser2.setText("激光笔2");
//				drawView.setLaser2(false);
//			}
//			break;
//		case R.id.button_save:// 保存图片
//			GeneratePic();
//			break;
//		default:
//			break;
//		}
//	}
}
