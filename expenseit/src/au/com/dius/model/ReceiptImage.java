package au.com.dius.model;

import java.io.Serializable;

public class ReceiptImage implements Serializable {

	private final String image;

	public ReceiptImage(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}
}
