/**
 * 
 */
package com.acktie.mobile.android.barcode;

import java.util.HashMap;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIView;

import com.acktie.mobile.android.camera.CameraManager;

import android.app.Activity;
import android.hardware.Camera;

/**
 * @author TNuzzi
 *
 */
@Kroll.proxy(creatableInModule = AcktiemobileandroidbarcodeModule.class)
public class BarcodeViewProxy extends TiViewProxy {
	private static final String LCAT = "BarcodeViewProxy";
	private CameraManager cameraManager = null;
	private BarcodeInputArgs args = null;
	
	/**
	 * 
	 */
	public BarcodeViewProxy() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.appcelerator.titanium.proxy.TiViewProxy#createView(android.app.Activity)
	 */
	@Override
	public TiUIView createView(Activity arg0) {
		Log.d(LCAT, "Creating BarcodeView");
		cameraManager = new CameraManager(args.getCameraDevice());
		TiUIView view = new BarcodeView(this, cameraManager, args);
		view.getLayoutParams().autoFillsHeight = true;
		view.getLayoutParams().autoFillsWidth = true;
		return view;
	}

	// Handle creation options
	@SuppressWarnings("rawtypes")
	@Override
	public void handleCreationDict(KrollDict options) 
	{
		super.handleCreationDict(options);
		
		args = new BarcodeInputArgs();
		
		if (hasProperty(BarcodeInputArgs.SUCCESS_CALLBACK)) {
			args.setSuccessCallback((KrollFunction) getProperty(BarcodeInputArgs.SUCCESS_CALLBACK));
		}
		if (hasProperty(BarcodeInputArgs.CANCEL_CALLBACK)) {
			args.setCancelCallback((KrollFunction) getProperty(BarcodeInputArgs.CANCEL_CALLBACK));
		}
		if (hasProperty(BarcodeInputArgs.ERROR_CALLBACK)) {
			args.setErrorCallback((KrollFunction) getProperty(BarcodeInputArgs.ERROR_CALLBACK));
		}
		if (hasProperty(BarcodeInputArgs.CONTINUOUS)) {
			args.setContinuous(TiConvert.toBoolean(getProperty(BarcodeInputArgs.CONTINUOUS)));
		}
		if (hasProperty(BarcodeInputArgs.USE_JIS_ENCODING)) {
			args.setUseJISEncoding(TiConvert.toBoolean(getProperty(BarcodeInputArgs.USE_JIS_ENCODING)));
		}
		if (hasProperty(BarcodeInputArgs.SCAN_FROM_IMAGE_CAPTURE)) {
			args.setScanFromImageCapture(TiConvert.toBoolean(getProperty(BarcodeInputArgs.SCAN_FROM_IMAGE_CAPTURE)));
		}
		if (hasProperty(BarcodeInputArgs.USE_FRONT_CAMERA)) {
			if(TiConvert.toBoolean(getProperty(BarcodeInputArgs.USE_FRONT_CAMERA)))
			{
				args.setCameraDevice(Camera.CameraInfo.CAMERA_FACING_FRONT);
			}
			else
			{
				args.setCameraDevice(Camera.CameraInfo.CAMERA_FACING_BACK);
			}
		}
		if (hasProperty(BarcodeInputArgs.BARCODES)) {
			args.setBarcodes((Object[]) getProperty(BarcodeInputArgs.BARCODES));
		}
		if (hasProperty(BarcodeInputArgs.OVERLAY)) {
			HashMap overlay = (HashMap) getProperty(BarcodeInputArgs.OVERLAY);
			
			if(overlay.containsKey(BarcodeInputArgs.OVERLAY_COLOR))
			{
				args.setColor((String) overlay.get(BarcodeInputArgs.OVERLAY_COLOR));
			}
			if(overlay.containsKey(BarcodeInputArgs.OVERLAY_LAYOUT))
			{
				args.setLayout((String) overlay.get(BarcodeInputArgs.OVERLAY_LAYOUT));
			}
			if(overlay.containsKey(BarcodeInputArgs.OVERLAY_IMAGE_NAME))
			{
				args.setImageName((String) overlay.get(BarcodeInputArgs.OVERLAY_IMAGE_NAME));
			}
			if(overlay.containsKey(BarcodeInputArgs.OVERLAY_ALPHA))
			{
				args.setAlpha(TiConvert.toFloat(overlay.get(BarcodeInputArgs.OVERLAY_ALPHA)));
			}
		}
	}
	
	@Kroll.method
	public void toggleLight()
	{
		cameraManager.toggleTorch();
	}
	
	@Kroll.method
	public void scanBarcode()
	{
		cameraManager.takePicture();
	}
	
	@SuppressWarnings("rawtypes")
	public void successCallback(HashMap results)
	{
		if(args.getSuccessCallback() != null)
		{
			args.getSuccessCallback().callAsync(getKrollObject(), results);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void cancelCallback()
	{
		cameraManager.stop();
		
		if(args.getCancelCallback() != null)
		{
			args.getCancelCallback().callAsync(getKrollObject(), new HashMap());
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void errorCallback()
	{
		if(args.getErrorCallback() != null)
		{
			args.getErrorCallback().callAsync(getKrollObject(), new HashMap());
		}
	}
	
	@Kroll.method
	public void stop()
	{
		cancelCallback();
		cameraManager.stop();
	}
}
