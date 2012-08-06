package com.ackite.mobile.android.barcode;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.zbar.Symbol;

import org.appcelerator.kroll.KrollFunction;

public class InputArgs {

	public static final String CONTINUOUS = "continuous";
	private boolean continuous = false;
	
	public static final String SCAN_FROM_IMAGE_CAPTURE = "scanBarcodeFromImageCapture";
	private boolean scanBarcodeFromImageCapture = false;
	
	public static final String USE_JIS_ENCODING = "useJISEncoding";
	private boolean useJISEncoding = false;
	
	public static final String BARCODES = "barcodes";
	@SuppressWarnings("rawtypes")
	private Object[] barcodes = null;
	private int[] intBarcodes = null;
 	
	public static final String SUCCESS_CALLBACK = "success";
	private KrollFunction successCallback = null;
	
	public static final String CANCEL_CALLBACK = "cancel";
	private KrollFunction cancelCallback = null;
	
	public static final String ERROR_CALLBACK = "error";
	private KrollFunction errorCallback = null;
	
	public static final String OVERLAY = "overlay";
	
	public static final String OVERLAY_COLOR = "color";
	private String color = null;
	
	public static final String OVERLAY_LAYOUT = "layout";
	private String layout = null;
	
	public static final String OVERLAY_IMAGE_NAME = "imageName";
	private String imageName = null;
	
	public static final String OVERLAY_ALPHA = "alpha";
	private float alpha = 1.0f;

	public boolean isContinuous() {
		return continuous;
	}

	public void setContinuous(boolean continuous) {
		this.continuous = continuous;
	}

	public boolean isScanBarcodeFromImageCapture() {
		return scanBarcodeFromImageCapture;
	}

	public void setScanBarcodeFromImageCapture(boolean scanBarcodeFromImageCapture) {
		this.scanBarcodeFromImageCapture = scanBarcodeFromImageCapture;
	}

	public boolean isUseJISEncoding() {
		return useJISEncoding;
	}

	public void setUseJISEncoding(boolean useJISEncoding) {
		this.useJISEncoding = useJISEncoding;
	}

	public KrollFunction getSuccessCallback() {
		return successCallback;
	}

	public void setSuccessCallback(KrollFunction successCallback) {
		this.successCallback = successCallback;
	}

	public KrollFunction getCancelCallback() {
		return cancelCallback;
	}

	public void setCancelCallback(KrollFunction cancelCallback) {
		this.cancelCallback = cancelCallback;
	}

	public KrollFunction getErrorCallback() {
		return errorCallback;
	}

	public void setErrorCallback(KrollFunction errorCallback) {
		this.errorCallback = errorCallback;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if(color.equalsIgnoreCase("blue"))
		{
			this.color = "Blue";
		}
		else if(color.equalsIgnoreCase("purple"))
		{
			this.color = "Purple";
		}
		else if(color.equalsIgnoreCase("red"))
		{
			this.color = "Red";
		}
		else if(color.equalsIgnoreCase("yellow"))
		{
			this.color = "Yellow";
		}
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		if(layout.equalsIgnoreCase("center"))
		{
			this.layout = "Center";
		}
		else if(layout.equalsIgnoreCase("full"))
		{
			this.layout = "FullScreen";
		}
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public Object[] getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(Object[] barcodes) {
		this.barcodes = barcodes;
		convertStringBarcodeNamesToInts();
	}

	private void convertStringBarcodeNamesToInts()
	{
		List<Integer> tempBarcode = new ArrayList<Integer>();
		for (int i = 0; i < barcodes.length; i++) {
			String barcode = (String) barcodes[i];
			
			if(barcode.equalsIgnoreCase("EAN8"))
			{
				tempBarcode.add(Symbol.EAN8);
			}
			else if(barcode.equalsIgnoreCase("EAN13"))
			{
				tempBarcode.add(Symbol.EAN13);
			}
			else if(barcode.equalsIgnoreCase("UPCA"))
			{
				tempBarcode.add(Symbol.UPCA);
			}
			else if(barcode.equalsIgnoreCase("UPCE"))
			{
				tempBarcode.add(Symbol.UPCE);
			}
			else if(barcode.equalsIgnoreCase("ISBN10"))
			{
				tempBarcode.add(Symbol.ISBN10);
			}
			else if(barcode.equalsIgnoreCase("ISBN13"))
			{
				tempBarcode.add(Symbol.ISBN13);
			}
			else if(barcode.equalsIgnoreCase("I25"))
			{
				tempBarcode.add(Symbol.I25);
			}
			else if(barcode.equalsIgnoreCase("DATABAR"))
			{
				tempBarcode.add(Symbol.DATABAR);
			}
			else if(barcode.equalsIgnoreCase("DATABAR_EXP"))
			{
				tempBarcode.add(Symbol.DATABAR_EXP);
			}
			else if(barcode.equalsIgnoreCase("PDF417"))
			{
				tempBarcode.add(Symbol.PDF417);
			}
			else if(barcode.equalsIgnoreCase("CODE39"))
			{
				tempBarcode.add(Symbol.CODE39);
			}
			else if(barcode.equalsIgnoreCase("CODE93"))
			{
				tempBarcode.add(Symbol.CODE93);
			}
			else if(barcode.equalsIgnoreCase("CODE128"))
			{
				tempBarcode.add(Symbol.CODE128);
			}
			else if(barcode.equalsIgnoreCase("CODABAR"))
			{
				tempBarcode.add(Symbol.CODABAR);
			}	
		}
		
		intBarcodes = convertIntegers(tempBarcode);
		
	}

	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}

	public int[] getIntBarcodes() {
		return intBarcodes;
	}

}
