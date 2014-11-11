package cashmanager.helo.com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by konstantin
 * Date: 19.06.14, 14:12
 */
public class ImageRetriever {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 101;

    private Activity mActivity;
    private Uri mImageUri;

    public ImageRetriever(Activity activity) {
        mActivity = activity;
    }

    public void getImageFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        mActivity.startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public Intent getImageFromCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        return cameraIntent;
    }

    public void getImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        mActivity.startActivityForResult(photoPickerIntent, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public Intent getImageGalleryIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        return photoPickerIntent;
    }

    public String getImagePath(int requestCode, int resultCode, Intent data) {

        Bitmap photo = null;
        String imageFilePath = "";
        if ((requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
            Uri _uri = data.getData();

            imageFilePath = getRealPathFromURI(_uri);
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            Uri tempUri = getImageUri(mActivity, photo);

            imageFilePath = getRealPathFromURI(tempUri);
        }
        if (photo != null) return imageFilePath;
        else return imageFilePath;
    }

    public Bitmap getBitmapFromPath(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mActivity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
