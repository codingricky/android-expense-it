package au.com.dius.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
			
		}).create();
		return gson.toJson(this);
	}
}
