package com.acktie.mobile.android.barcode;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.zbar.Symbol;

import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutArrangement;

import com.acktie.mobile.android.camera.CameraCallback;
import com.acktie.mobile.android.camera.CameraManager;
import com.acktie.mobile.android.camera.CameraSurfaceView;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class BarcodeView extends TiUIView {

	CameraSurfaceView cameraPreview = null;
	CameraManager cameraManager = null;
	

	private static final int ALL_BARCODE_SYMBOLS[] = 
	{
	    Symbol.EAN8,
	    Symbol.EAN13,
	    Symbol.UPCA,
	    Symbol.UPCE,
	    Symbol.ISBN10,
	    Symbol.ISBN13,
	    Symbol.I25,
	    Symbol.DATABAR,
	    Symbol.DATABAR_EXP,
	    Symbol.PDF417,
	    Symbol.CODE39,
	    Symbol.CODE93,
	    Symbol.CODE128,
	    Symbol.CODABAR,
	};

	public BarcodeView(TiViewProxy proxy, final CameraManager cameraManager,
			BarcodeInputArgs args) {
		super(proxy);
		this.cameraManager = cameraManager;
		final BarcodeViewProxy barcodeCodeViewProxy = (BarcodeViewProxy) proxy;
		LayoutArrangement arrangement = LayoutArrangement.DEFAULT;

		if (proxy.hasProperty(TiC.PROPERTY_LAYOUT)) {
			String layoutProperty = TiConvert.toString(proxy
					.getProperty(TiC.PROPERTY_LAYOUT));
			if (layoutProperty.equals(TiC.LAYOUT_HORIZONTAL)) {
				arrangement = LayoutArrangement.HORIZONTAL;
			} else if (layoutProperty.equals(TiC.LAYOUT_VERTICAL)) {
				arrangement = LayoutArrangement.VERTICAL;
			}
		}

		proxy.getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		TiCompositeLayout layout = new TiCompositeLayout(proxy.getActivity(),
				arrangement);

		int[] allBarcodes = ALL_BARCODE_SYMBOLS;
		if(args.getIntBarcodes() != null)
		{
			allBarcodes = args.getIntBarcodes();
		}
		
		CameraCallback cameraCallback = new CameraCallback(allBarcodes,
				barcodeCodeViewProxy, cameraManager, args);
		cameraManager.setCameraCallback(cameraCallback);
		cameraPreview = new CameraSurfaceView(proxy.getActivity(),
				cameraCallback, cameraManager);

		layout.addView(cameraPreview);
		
		URL urlPath = getImageURLPath(args);

		if (urlPath != null) {
			try {
				URLConnection urlConnection = urlPath.openConnection();
				Bitmap myBitmap = BitmapFactory.decodeStream(urlConnection
						.getInputStream());
				ImageView imageView = new ImageView(proxy.getActivity());
				imageView.setImageBitmap(myBitmap);
				imageView.setAlpha(convertFloatToIntForAlpha(args.getAlpha()));
				layout.addView(imageView);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: Log?
			}
		}

		setNativeView(layout);
	}

	private URL getImageURLPath(BarcodeInputArgs args) {
		String imageName = args.getImageName();
		URL url = null;
		if(imageName != null)
		{
			url = getClass().getResource("/assets/Resources/modules/com.acktie.mobile.android.barcode/" + imageName);
		}
		else
		{
			String color = args.getColor();
			String layout = args.getLayout();
			
			if(color != null && layout != null)
			{
				url = getClass().getResource("/assets/" + layout + "-" + color + ".png");
			}
		}
		
		return url;
	}
	
	private int convertFloatToIntForAlpha(float floatAlpha)
	{
		float maxAlpha = 255.0f;
		
		return (int) (maxAlpha * floatAlpha);
		
	}
}
