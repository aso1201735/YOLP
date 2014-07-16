package jp.ac.st.asojuku.yolp001;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity implements LocationListener{

	//LocationManagerを準備
	LocationManager mLocationManager = null;
	//MapViewを準備
	MapView mMapView = null;
	//直前の緯度(1000000倍精度)
	int lastLatitude = 0;
	//直前の経度(1000000倍精度)
	int lastLongitude = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();

		//地図表示用のyahooライブラリview部品を用意
		mMapView = new MapView(this,"kakig111");
		//ズームボタンを画面にON
		mMapView.setBuiltInZoomControls(true);
		//地図縮尺バーを画面にON
		mMapView.setScalebar(true);

		//ここから、手動で地図をセット
		//渋谷駅の緯度経度のGeoPointを手書きで設定
		double lat = 35.658516;
		double lon = 139.701773;
		GeoPoint gp = new GeoPoint((int)(lat * 1000000),(int)(lon * 100000));
		//地図本体を取得
		MapController c = mMapView.getMapController();

		//地図本体にGeoPointoを設定
		c.setCenter(gp);
		//地図本体のズームを3に設定
		c.setZoom(3);
		//地図本体を画面にセット
		setContentView(mMapView);

		//ここからGPS使用
		//LocationManagerを取得
		mLocationManager =
				(LocationManager) getSystemService(Context.LOCATION_SERVICE);

		//位置測定のためのGPS精度や使用消費電力を設定するふるいにかけるためのCriteriaオブジェクトを作成
		Criteria criteria = new Criteria();
		//Accuracyを指定(低精度)
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		//PowerRequirementを指定(低消費電力)
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		//位置情報を伝達してくれるロケーションプロバイダの取得
		String provider = mLocationManager.getBestProvider(criteria, true);
		//位置情報のイベントリスナーであるLocationListenerを登録
		mLocationManager.requestLocationUpdates(provider, 0, 0,this);


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location){

		//緯度の取得
		double lat = location.getLatitude();
		int latitude = (int)(lat * 1000000);
		//経度の取得
		double lon = location.getLongitude();
		int longitude = (int)(lon * 1000000);

		//緯度と経度のいずれかが直前の値と誤差が出れば画面を更新(100で割って元々の緯度経度少数４桁、100mくらいの誤差にする)
		if(latitude/1000 != this.lastLatitude/1000 || longitude/1000 != this.lastLongitude/1000){
			//緯度経度情報(GeoPoint)の作成
			GeoPoint gp = new GeoPoint(latitude, longitude);
			//地図本体を取得
			MapController c = mMapView.getMapController();
			//地図本体にGeoPointoを設定
			c.setCenter(gp);

			//今回の緯度経度を覚える
			this.lastLatitude = latitude;
			this.lastLongitude = longitude;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}


}
