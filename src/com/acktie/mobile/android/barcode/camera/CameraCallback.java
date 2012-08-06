package com.acktie.mobile.android.barcode.camera;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ackite.mobile.android.barcode.BarcodeViewProxy;
import com.ackite.mobile.android.barcode.InputArgs;
import com.acktie.mobile.android.barcode.zbar.ZBarManager;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;

public class CameraCallback implements PreviewCallback {

	private CameraManager cameraManager = null;
	private ImageScanner scanner = null;
	private BarcodeViewProxy viewProxy = null;
	private InputArgs args = null;
	private long lastBarcodeDetected = System.currentTimeMillis();
	private boolean pictureTaken = false;

	public CameraCallback(int[] symbolsToScan, BarcodeViewProxy viewProxy,
			CameraManager cameraManager, InputArgs args) {

		/* Instance barcode scanner */
		scanner = ZBarManager.getImageScannerInstance(symbolsToScan);
		this.cameraManager = cameraManager;
		this.viewProxy = viewProxy;
		this.args = args;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// These could have been || (or'ed) together but I wanted to make it
		// easier
		// to understand why the image was not processed
		if (hasEnoughTimeElapsedToScanNextImage()) {
			return;
		} else if (args.isScanBarcodeFromImageCapture() && !pictureTaken) {
			return;
		}

		scanImageForBarcode(data, camera, pictureTaken);

		pictureTaken = false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void scanImageForBarcode(byte[] data, Camera camera, boolean fromPictureTaken)
	{
		if(cameraManager.isStopped()) {
			return;
		}
		
		Camera.Parameters parameters = cameraManager.getCameraParameters();
		
		// If null, likely called after camera has been released.
		if(parameters == null)
		{
			return;
		}
		
		Size size = parameters.getPreviewSize();

		// Supported image formats
		// http://sourceforge.net/apps/mediawiki/zbar/index.php?title=Supported_image_formats
		Image barcode = ZBarManager.getImageInstance(size.width, size.height,
				ZBarManager.Y800, data);

		int result = scanner.scanImage(barcode);

		int quality = 0;
		Symbol symbol = null;

		if (result != 0) {
			SymbolSet syms = scanner.getResults();
			for (Symbol sym : syms) {
				System.out.println("Quality of Scan (Higher than 0 is good): "
						+ sym.getQuality());
				if (sym.getQuality() > quality) {
					symbol = sym;
				}
			}

			if (viewProxy != null && symbol != null) {

				Charset cs = Charset.forName("UTF-8");;
				String resultData = new String(symbol.getData().getBytes(), cs);
				
				if(args.isUseJISEncoding())
				{
					cs = Charset.forName("Shift_JIS");
					resultData = new String(symbol.getData().getBytes(), cs);
				}

				System.out.println(resultData);

				if(!args.isContinuous())
				{
					cameraManager.stop();
				}

				HashMap results = new HashMap();

				results.put("data", resultData);
				results.put("type", getTypeNameFromType(symbol.getType()));

				viewProxy.successCallback(results);

				lastBarcodeDetected = getOneSecondFromNow();
			}
		}
		else if(fromPictureTaken)
		{
			viewProxy.errorCallback();
		}
	}

	private boolean hasEnoughTimeElapsedToScanNextImage() {
		return lastBarcodeDetected > System.currentTimeMillis();
	}

	private long getOneSecondFromNow() {
		return System.currentTimeMillis() + 3000;
	}

	private String getTypeNameFromType(int type) {
		switch (type) {
		case Symbol.EAN8:
			return "EAN-8";
		case Symbol.EAN13:
			return "EAN-13";
		case Symbol.UPCA:
			return "UPC-A";
		case Symbol.UPCE:
			return "UPC-E";
		case Symbol.ISBN10:
			return "ISBN-10";
		case Symbol.ISBN13:
			return "ISBN-13";
		case Symbol.I25:
			return "I2/5";
		case Symbol.DATABAR:
			return "DataBar";
		case Symbol.DATABAR_EXP:
			return "DataBar-Exp";
		case Symbol.PDF417:
			return "PDF417";
		case Symbol.CODE39:
			return "CODE-39";
		case Symbol.CODE93:
			return "CODE-93";
		case Symbol.CODE128:
			return "CODE-128";
		case Symbol.CODABAR:
			return "Codabar";
		}
		
		return "";
	}

	public void setPictureTaken(boolean pictureTaken) {
		this.pictureTaken = pictureTaken;
	}
}
