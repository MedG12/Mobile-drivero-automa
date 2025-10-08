package com.automa.ui.driver.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.automa.domain.driver_task.model.WorkOrderModel
import com.automa.ui.R
import com.automa.ui.databinding.ActivityLocationDetailBinding
import com.automa.ui.utils.MapUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.roundToInt

class LocationDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        fun newIntent(context: Context, workOrderData: WorkOrderModel): Intent {
            return Intent(context, LocationDetailActivity::class.java).apply {
                putExtra(WO_DATA, workOrderData)
            }
        }

        private const val WO_DATA = "WO_DATA"
    }

    private lateinit var binding: ActivityLocationDetailBinding

    private lateinit var gMap: GoogleMap
    private lateinit var woData: WorkOrderModel
    private val listMarker = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        woData = intent.getParcelableExtra(WO_DATA)!!

        binding.toolbarLocationDetail.setOnNavigationClickListener { finish() }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        val circleSize = (resources.displayMetrics.density*11).roundToInt()
        val iconCircle = ContextCompat.getDrawable(this, R.drawable.bg_circle)
        woData.result.forEachIndexed { index, data ->
            val latLng = LatLng(data.lat, data.lng)
            try {
                gMap.addMarker(MarkerOptions().apply {
                    icon(MapUtils.bitmapDescriptorFromVector(iconCircle, circleSize, circleSize))
                    position(latLng)
                }).also { m -> if (m!=null) listMarker.add(m) }
                if (index==0) {
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
            } catch (e: Exception) {
                Log.d("TAG-MAP", "initObserver: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}