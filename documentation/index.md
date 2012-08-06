# Acktie Mobile Barcode Module

## Description

This module allows for a quick integration of a barcode reader into your Appcelerator Mobile application.  The barcode reading ability comes in three scanning modes and supports many barcode standards.

*  Scan from Camera Feed
*  Read image from Camera image capture (User manually clicks scan Barcode button)

Support Barcode standards are:

*  EAN-2
*  EAN-5 
*  EAN-8 
*  UPC-E 
*  ISBN-10 
*  UPC-A 
*  EAN-13 
*  ISBN-13 
*  COMPOSITE 
*  I2/5 
*  DataBar 
*  DataBar-Exp 
*  Codabar 
*  CODE-39 
*  CODE-93 
*  CODE-128 
*  PDF417 

Additionally, the both options has the ability to provide an overlay on the feed.  Several colors and layout are provide by default with the module.  
Documented below are a list of the preloaded overlays.

**NOTE**: This module was developed using a Nexus S and LG Optimus Elite

**NOTE**: The current version of the Android Barcode module does not support the equivalent iOS feature of scanning an image from the Android Image Gallary.  We discovered that it did not provide a good user experience on lower 
end Android device.

## Accessing the Acktie Mobile Barcode Module

To access this module from JavaScript, you would do the following:

	var barcodeReader = require("com.acktie.mobile.android.barcode");

The barcode reader variable is a reference to the Module object.	

## Reference

The following are the Javascript functions you can call with the module.

All of the modules provide callbacks for:

### success (Callack)
Called in the event of a successful scan.  

#### result (callback result)

*  data - This returns the data of the barcode scan.
*  type - This is the type of barcode that was detected

Example: 

	function success(data){
		var barcodeData = data.data;
		var barcodeType = data.type
	};

Valid barcode "type" string returned from module :

*  EAN-2
*  EAN-5 
*  EAN-8 
*  UPC-E 
*  ISBN-10 
*  UPC-A 
*  EAN-13 
*  ISBN-13 
*  COMPOSITE 
*  I2/5 
*  DataBar 
*  DataBar-Exp 
*  Codabar 
*  CODE-39 
*  CODE-93 
*  CODE-128 
*  PDF417 


### cancel (Callack)
Called if the user clicks the cancel button.

*NOTE*: No callback data returned.

### error 
Called if the scan was not successful in reading the barcode.

*NOTE*: No callback data returned.

### createBarcodeView

This function returns a view (TIView) that scans a Barcode code from the live Camera Feed.  Unlike the iOS version, this single view will be used for the automatic Barcode detection as well as the 
manual user capture detection.

Since this view is based off the [Titanium.UI.View](http://docs.appcelerator.com/titanium/2.1/index.html#!/api/Titanium.UI.View) you can the majority of Titanium.UI.View to set the 
size and look of the window.  NOTE: We have not tested all the view options against the BarcodeView but the basic ones we used (size and position) have work fine.

Example of Scanning from Camera without overlay:

	var options = {
		// ** Android Barcode Reader properties (ignored by iOS)
		backgroundColor : 'black',
		width : '100%',
		height : '90%',
		top : 0,
		left : 0,
		// **

		// ** Used by both iOS and Android
		success : success,
		cancel : cancel,
		error : error,
	};
	
	createBarcodeView(options);


Example of Scanning from Camera with overlay:

	var options = {
		// ** Android Barcode Reader properties (ignored by iOS)
		backgroundColor : 'black',
		width : '100%',
		height : '90%',
		top : 0,
		left : 0,
		// **

		// ** Used by both iOS and Android
		overlay : {
			color : "blue",
			layout : "center",
			alpha : .75
		},
		success : success,
		cancel : cancel,
		error : error,
	};
	createBarcodeView(options);

#### Valid options for createBarcodeView

#### Valid overlay options

The following are the value JSON values for overlay.

#### color (optional):
*  blue
*  purple
*  red
*  yellow

#### layout (optional):
*  full
*  center

NOTE: Both color and layout must be specified together.

#### imageName (optional):
Use this property if you want to use your own overlay image.  See the customize overlay section for more details.

#### alpha (optional):
A float value between 0 - 1.  0 being fully transparent and 1 being fully visible.

Example:

alpha: 0.5  // half transparent

#### Valid additional options for createBarcodeView

#### scanBarcodeFromImageCapture (optional):
Use this property to indicate to the View to NOT auto-detect the Barcode Code.  This property is used in conjuction with the scanBarcode() function.

Scans a Barcode from an image taken from the Camera.  The user will have to manually click scan (scanBarcode()) for the Barcode to be scanned.

Example:

	var options = {
		// ** Android Barcode Reader properties (ignored by iOS)
		backgroundColor : 'black',
		width : '100%',
		height : '90%',
		top : 0,
		left : 0,
		scanBarcodeFromImageCapture : true,
		// **

		// ** Used by both iOS and Android
		scanButtonName : 'Scan Code!',
		success : success,
		cancel : cancel,
		error : error,
	};
	
	var barcodeView = createBarcodeView(options);
	
	...
	
	var scanBarcode = Titanium.UI.createButton({
		title : options.scanButtonName,
		bottom : 0,
		left : '40%'
	});

	scanBarcode.addEventListener('click', function() {
		barcodeView.scanBarcode();
	});

#### continuous (optional):
This feature will continuously scan for Barcodes even after one has been detected.  The user will have to click the a button (stop()) to exit the scan screen.
With each Barcode that is detected the "success" event will be triggers so you program will be able to process each Barcode.  Also, the application can use
the phone virate feature to indicate a scan took place.  See example app.js for details.  Additionally, if this property is not used the Camera will 
automatically stop scanning.

Example:

continuous: true,

By default this value is false.

#### barcodes (optional):
An array of strings contain valid barcode types the modules should use for detection.  If the type is not specified it will not be detected.
The following are a list of valid types:

*  "EAN2",
*  "EAN5",
*  "EAN8",
*  "UPCE",
*  "ISBN10",
*  "UPCA",
*  "EAN13",
*  "ISBN13",
*  "COMPOSITE",
*  "I25",
*  "DATABAR",
*  "DATABAR_EXP",
*  "CODE39",
*  "PDF417",
*  "CODE93",
*  "CODE128",

The app.js has several examples of different barcode types grouped.

NOTE about ISBN:  This type is detected through the EAN13 algorithm.  If you want to detect ISBN10 or ISBN13 then you will need to include EAN13 in your barcodes array list.
Also, ISBN10 has priority over ISBN13 so ISBN10 data and type will be returns when both are detected. 

Default for this property is all the available barcode types.

Example:

Only detect UPC barcodes:

	...
	
	var UPC = [
    	"UPCE",
    	"UPCA",];
	
	...

	var options = {
		// ** Android Barcode Reader properties (ignored by iOS)
		backgroundColor : 'black',
		width : '100%',
		height : '90%',
		top : 0,
		left : 0,
		// **

		// ** Used by both iOS and Android
		barcodes: UPC,
		success : success,
		cancel : cancel,
		error : error,
	};
	
	var barcodeView = createBarcodeView(options);

#### Valid functions for createBarcodeView object

#### toggleLight (Function on createBarcodeView)
This function can be used to control the light on the camera.  

Example:

	...
	var barcodeView = createBarcodeView(options);
	
	var lightToggle = Ti.UI.createSwitch({
		value : false,
		bottom : 0,
		right : 0
	});
	
	lightToggle.addEventListener('change', function() {
		barcodeView.toggleLight();
	})
	

#### scanBarcode (Function on createBarcodeView)
This function is used to manually scan an image and used in conjunction with the property scanBarcodeFromImageCapture.  
This function will most likely be in the addEventListener for a button.

For example see "scanBarcodeFromImageCapture".

#### stop (Function on createBarcodeView)
This function is used to stop the Camera in the Barcode View and release all Camera resources.  Additionally, the module will call the "cancel" callback (if passed).
If the user has an option to navigate away from the Barcode View it is advised you call the "stop" function.  Just call "createBarcodeView" to re-enable the barcode View.

Example:

	barcodeView = barcodreader.createBarcodeView(options);

	var closeButton = Titanium.UI.createButton({
		title : "close",
		bottom : 0,
		left : 0
	});

	closeButton.addEventListener('click', function() {
		barcodeView.stop();
	});

## Customize Overlay
  In order to customize the overlay you will need to do 2 things:

-  Create a directory under your mobile app's "Resources" directory called "modules/com.acktie.mobile.android.barcode".  This is the directory
   where you will put your custom images.
-  Use the property "imageName" in the createBarcodeView arguments (see above).

Example:

	var options = {
		// ** Android Barcode Reader properties (ignored by iOS)
		backgroundColor : 'black',
		width : '100%',
		height : '90%',
		top : 0,
		left : 0,
		// **

		// ** Used by both iOS and Android
		barcodes: UPC,
		overlay: {
			imageName: "myOverlay.png",
			alpha: 0.35f
		},
		success : success,
		cancel : cancel,
		error : error,
	};
	
	var barcodeView = createBarcodeView(options);
	
NOTE: Specifying an imageName will override any color/layout that is also specified in the same overlay property. Meaning, when they are both specified imageName will take precedence.

However, alpha works on both regardless of what is used (color/layout or imageName).

Included in the example/images subdirectory is an example Photoshop file and .png files. 

## Known Issues:

### Default Images (ones that come with the module) are not showing up
It seems there is a bug in the build process where on a non-full build the module assests are not being copied over into the .apk.  As a result,
the module will not display the overlays that come with the module.  Currently, the only way to fix this situation is to force a "full build" (e.g. delete
the build/android directory or modify the tiapp.xml (add something save, remove it save)).

NOTE: This issue does not affect the custom images (e.g. imageName) you provide in your mobile project.  Also, there is no adverse affect to your mobile application
if the images are missing (other than the overlays not showing up).

## Change Log
*  1.0 Initial Release

## Author

Tony Nuzzi @ Acktie

Twitter: @Acktie

Email: support@acktie.com