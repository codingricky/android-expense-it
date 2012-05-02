package au.com.dius.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Expense {
	private String name;
	private List<Receipt> receipts;
	
	public Expense(String name, List<Receipt> receipts) {
		super();
		this.name = name;
		this.receipts = receipts;
	}

	public String serialize() {
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new TypeAdapter<Date>() {

			@Override
			public Date read(JsonReader reader) throws IOException {
				throw new IllegalStateException("Not implemented");
			}

			@Override
			public void write(JsonWriter reader, Date date) throws IOException {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				reader.value(dateFormat.format(date));
			}
			
		}).registerTypeAdapter(ReceiptImage.class, new TypeAdapter<ReceiptImage>() {

			@Override
			public ReceiptImage read(JsonReader reader) throws IOException {
				throw new IllegalStateException("Not implemented");
			}

			@Override
			public void write(JsonWriter reader, ReceiptImage receiptImage) throws IOException {
				String path = receiptImage.getImage();
				if (path != null && !path.isEmpty()) {
					byte[] fileAsBytes = getBytesFromFile(new File(path));
					reader.value(Base64.encodeToString(fileAsBytes, Base64.DEFAULT));	
				} else {
					reader.value("");
				}
			}
			
			private byte[] getBytesFromFile(File file) throws IOException {
			    InputStream is = new FileInputStream(file);

			    // Get the size of the file
			    long length = file.length();

			    // You cannot create an array using a long type.
			    // It needs to be an int type.
			    // Before converting to an int type, check
			    // to ensure that file is not larger than Integer.MAX_VALUE.
			    if (length > Integer.MAX_VALUE) {
			        // File is too large
			    }

			    // Create the byte array to hold the data
			    byte[] bytes = new byte[(int)length];

			    // Read in the bytes
			    int offset = 0;
			    int numRead = 0;
			    while (offset < bytes.length
			           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			        offset += numRead;
			    }

			    // Ensure all the bytes have been read in
			    if (offset < bytes.length) {
			        throw new IOException("Could not completely read file "+file.getName());
			    }

			    // Close the input stream and return bytes
			    is.close();
			    return bytes;
			}
		}).create();
		return gson.toJson(this);
	}
}
