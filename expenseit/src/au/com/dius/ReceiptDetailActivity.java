package au.com.dius;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import au.com.dius.model.Receipt;
import au.com.dius.sql.ReceiptDataSource;

public class ReceiptDetailActivity extends Activity {

	private static final String TAG = "ReceiptDetailActivity";

	private static final int ACTIVITY_SELECT_IMAGE = 0;

	private static final int TAKE_PICTURE = 1;

	private ReceiptDataSource receiptDataSource;

	private Spinner clientSpinner;
	private Spinner categorySpinner;
	private DatePicker datePicker;
	private TextView amountTextView;
	private TextView descriptionTextView;
	private ImageView photoImageView;
	private Receipt receipt;
	private Uri outputFileUri;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		receiptDataSource = new ReceiptDataSource(this);
		receiptDataSource.open();

		setContentView(R.layout.receiptdetail);

		clientSpinner = (Spinner) findViewById(R.id.clientSpinner);
		ArrayAdapter<CharSequence> clientAdapter = ArrayAdapter.createFromResource(this, R.array.client_array, android.R.layout.simple_spinner_item);
		clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clientSpinner.setAdapter(clientAdapter);

		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(categoryAdapter);

		datePicker = (DatePicker) findViewById(R.id.datePicker);
		amountTextView = (TextView) findViewById(R.id.numberTextView);
		descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		photoImageView = (ImageView) findViewById(R.id.photoImageView);
		photoImageView.setAdjustViewBounds(true);

		if (getIntent().getExtras() != null) {
			receipt = (Receipt) getIntent().getExtras().get("au.com.dius.Receipt");
			mapReceiptToGui();
		}

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				save();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				cancel();
			}
		});

		Button attachImageButton = (Button) findViewById(R.id.attachImageButton);
		attachImageButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				attachImage();
			}
		});

		Button photoButton = (Button) findViewById(R.id.photoButton);
		photoButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				takePhoto();
			}
		});
	}

	private void attachImage() {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
	}

	private void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

		outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
			outputFileUri = Uri.fromFile(new File(getPath(imageReturnedIntent.getData())));
			photoImageView.setImageURI(outputFileUri);
		} else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
			Log.d("takePhoto", "photo taken");
			photoImageView.setImageURI(outputFileUri);
		} else {
			outputFileUri = null;
		}
	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	private void mapReceiptToGui() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(receipt.getDate());
		datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

		float amountInDollars = receipt.getAmountInCents() / 100f;
		amountTextView.setText(String.valueOf(amountInDollars));

		setSelectedItem(clientSpinner, receipt.getClient());
		setSelectedItem(categorySpinner, receipt.getCategory());

		descriptionTextView.setText(receipt.getDescription());

		File file = new File(receipt.getImagePath());
		if (file.exists()) {
			photoImageView.setImageURI(Uri.fromFile(file));
			outputFileUri = Uri.fromFile(file);
		}
	}

	private void setSelectedItem(Spinner spinner, String item) {
		for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
			if (spinner.getAdapter().getItem(i).equals(item)) {
				spinner.setSelection(i);
			}
		}
	}

	private void save() {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		final Calendar enteredDate = Calendar.getInstance();
		enteredDate.set(Calendar.DAY_OF_MONTH, day);
		enteredDate.set(Calendar.MONTH, month);
		enteredDate.set(Calendar.YEAR, year);

		String amount = amountTextView.getText().toString();
		int amountInCents = (int) (Float.parseFloat(amount) * 100);

		String imagePath = "";
		if (outputFileUri != null) {
			imagePath = outputFileUri.getPath();
		}
		Log.d(TAG, "imagePath=" + imagePath);
		
		if (receipt != null) {
			receipt = new Receipt(receipt.getId(), clientSpinner.getSelectedItem().toString(), categorySpinner.getSelectedItem().toString(), enteredDate.getTime(), amountInCents, descriptionTextView
					.getText().toString(), imagePath);
		} else {
			receipt = new Receipt(clientSpinner.getSelectedItem().toString(), categorySpinner.getSelectedItem().toString(), enteredDate.getTime(), amountInCents, descriptionTextView.getText()
					.toString(), imagePath);
		}
		receiptDataSource.saveReceipt(receipt);
		receiptDataSource.close();
		finish();
	}

	private void cancel() {
		receiptDataSource.close();
		finish();
	}

	@Override
	protected void onResume() {
		receiptDataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		receiptDataSource.close();
		super.onPause();
	}
}
