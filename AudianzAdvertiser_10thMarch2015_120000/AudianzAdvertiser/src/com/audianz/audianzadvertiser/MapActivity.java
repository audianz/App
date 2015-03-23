package com.audianz.audianzadvertiser;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.audianz.constants.APIConstant;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.UIStateMachine;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class MapActivity extends BaseActivity {


	private Button promote_btn				   = null;
	private Button map_help_btn                    = null;
	private TextView land_msg =null;
	private TextView header_map;
	static final LatLng delhi = new LatLng(APIConstant.DELHI_LATITUDE,APIConstant.DELHI_LONGITUDE); 
	private LatLng currentLocation=null;
	private GoogleMap googleMap =null;
	private AudianzLocation aLocation=null;

	private Engine engObj =null;
	private UIStateMachine 			uistate					= null;
	private ELogger 				mELogger 				= null;
	private final String 			TAG 					= "MAPACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		uistate=UIStateMachine.getInstance();
		uistate.curActivity= this;

		if(engObj==null)
			engObj= Engine.engObj;

		if(mELogger == null)
			mELogger = new ELogger();
		mELogger.setTag(TAG);
		
		getWidgedID();
		setListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setMap();
	}

	
	/**
	 * This method is used to display map on screen.
	 */

	private void setMap()
	{

		aLocation = new AudianzLocation(MapActivity.this);

		if(aLocation.canGetLocation())
		{
			double lat = aLocation.getLatitude();
			double lon = aLocation.getLongitude();
			if(lat!=0 || lon!=0)
			{
				currentLocation=new LatLng(lat,lon);
				Engine.engObj.cnfigReaderObj.setLATITUDE(lat);
				Engine.engObj.cnfigReaderObj.setLONGITUDE(lon);

			}
			else
			{
				currentLocation = delhi;
				Engine.engObj.cnfigReaderObj.setLATITUDE(currentLocation.latitude);
				Engine.engObj.cnfigReaderObj.setLONGITUDE(currentLocation.longitude);
			}

		}
		else
		{
			currentLocation=delhi;
			Engine.engObj.cnfigReaderObj.setLATITUDE(currentLocation.latitude);
			Engine.engObj.cnfigReaderObj.setLONGITUDE(currentLocation.longitude);
		}


		if(googleMap==null)
		{
			displayToast("Failed to load map", Gravity.CENTER, false,Toast.LENGTH_SHORT);
		}
		else
		{

			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
			googleMap.addMarker(new MarkerOptions()
			.position(currentLocation)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.promote_red))
					);

		}
	}
	private void getWidgedID(){
		promote_btn = (Button)findViewById(R.id.promote_btn);
		//	back_btn = (Button)findViewById(R.id.back_btn);
		land_msg = (TextView)findViewById(R.id.txt_land_msg);
		map_help_btn   =(Button)findViewById(R.id.map_help_btn);
		googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		header_map = (TextView)findViewById(R.id.header_map);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		promote_btn.setTypeface(custom_font);
		map_help_btn.setTypeface(custom_font);
		header_map.setTypeface(custom_font);
		Typeface  proxima = Typeface.createFromAsset(getAssets(),
				"fonts/MarkSimonsonProximaNovaRegular.otf");
		land_msg.setTypeface(proxima);

	}

	private void setListener(){
		promote_btn.setOnClickListener(mCListener);
		map_help_btn.setOnClickListener(mCListener);

		googleMap.setOnMapClickListener(mapClickListener);
		googleMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {
				// TODO Auto-generated method stub

				Location  location  = googleMap.getMyLocation();
				if(location !=null)
				{
					LatLng latlon = new LatLng(location.getLatitude(), location.getLongitude());
					googleMap.clear();
					googleMap.setMyLocationEnabled(true);
					googleMap.getUiSettings().setZoomControlsEnabled(false);
					googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 12));
					googleMap.addMarker(new MarkerOptions()
					.position(latlon)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.promote_red))
							);	
					
					/*try {
						Geocoder address = new Geocoder(getApplicationContext());
						List<Address> matches = address.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
						Address bestmatch = (matches.isEmpty() ? null : matches.get(0));
						
						displayToast(bestmatch.getAddressLine(0), Gravity.CENTER, false, Toast.LENGTH_LONG);
						
					} catch (Exception e) {
						// TODO: handle exception
						displayToast(e.getMessage(),Gravity.CENTER, false, Toast.LENGTH_SHORT);
					}
					*/
					
					Engine.engObj.cnfigReaderObj.setLATITUDE(location.getLatitude());
					Engine.engObj.cnfigReaderObj.setLONGITUDE(location.getLongitude());
				}
				/*else
				{
					displayToast("Turn on your location sharing", Gravity.CENTER,false,Toast.LENGTH_SHORT);	
				}*/
				return true;
			}
		});
	
	}

	GoogleMap.OnMapClickListener mapClickListener = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng location) {
			// TODO Auto-generated method stub
			googleMap.clear();
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
			googleMap.addMarker(new MarkerOptions()
			.position(location)
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.promote_red))
					);
			Engine.engObj.cnfigReaderObj.setLATITUDE(location.latitude);
			Engine.engObj.cnfigReaderObj.setLONGITUDE(location.longitude);
		}
	};

	View.OnClickListener mCListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.promote_btn:
				statMachineObj.nextState(MapActivity.this, UIEventType.PROMOTE, true, UIType.MAP_ACTIVITY,null);
				break;
			case R.id.map_help_btn:
				uistate.nextState(MapActivity.this, UIEventType.HELP_CLICK, false, UIType.MAP_ACTIVITY,null);
				break;
				/*case R.id.back_btn:
				finish();
				break;
				 */
			default:
				break;
			}
		}
	};


	private class AudianzLocation extends Service implements LocationListener
	{
		private final Context mContext;
		// Flag for GPS Status
		boolean isGPSEnabled=false;

		// Flag for Network Status
		boolean isNetworkEnabled =false;

		boolean canGetLocation = false;

		Location location;
		double latitude;
		double longitude;

		// The minimum distance to change Updates in meters
		private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE=10; 

		// Minimum time between updates in milliseconds
		private static final long MIN_TIME_BW_UPDATES= 1000*60*1;

		protected LocationManager locationManager;

		public AudianzLocation(Context context)
		{
			this.mContext = context;
			getLocation();
		}

		public Location getLocation()
		{

			try{

				locationManager =(LocationManager)mContext.getSystemService(LOCATION_SERVICE);

				isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

				isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

				if(!isGPSEnabled && !isNetworkEnabled)
				{
					mELogger.error("Unable to find location GPS: "+isGPSEnabled+"  or Network "+isNetworkEnabled);
				}
				else
				{

					this.canGetLocation=true;
					if(isNetworkEnabled)
					{
						locationManager.requestLocationUpdates
						(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
						if(locationManager!=null)
						{
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if(location!=null)
							{
								latitude  = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}

					if(isGPSEnabled)
					{
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
						if(locationManager!=null)
						{
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if(location!=null)
							{
								latitude  = location.getLatitude();
								longitude = location.getLongitude();
							}
						}

					}

				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
				mELogger.error("Exception "+e.getMessage());
			}

			return location;
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean canGetLocation() {
			return this.canGetLocation;
		}
		public double getLatitude()
		{
			if(location!=null)
				latitude = location.getLatitude();

			return latitude;
		}

		public double getLongitude()
		{
			if(location!=null)
				longitude = location.getLongitude();

			return longitude;
		}
	}


}
