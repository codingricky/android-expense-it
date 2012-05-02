package au.com.dius.model;

import java.io.Serializable;
import java.util.Date;

public class Receipt implements Serializable {
	
	private long id = -1;
	private String client;
	private String category;
	private Date date;
	private int amountInCents;
	private String description;

	public Receipt(String client, String category, Date date, int amountInCents, String description) {
		this.client = client;
		this.category = category;
		this.date = date;
		this.amountInCents = amountInCents;
		this.description = description;
	}

	public Receipt(long id, String client, String category, Date date, int amountInCents, String description) {
		this.id = id;
		this.client = client;
		this.category = category;
		this.date = date;
		this.amountInCents = amountInCents;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClient() {
		return client;
	}

	public String getCategory() {
		return category;
	}

	public Date getDate() {
		return date;
	}

	public int getAmountInCents() {
		return amountInCents;
	}

	public String toString() {
		return client + " " + category + " " + amountInCents + " " + date;
	}

	public String getDescription() {
		return description;
	}
}
