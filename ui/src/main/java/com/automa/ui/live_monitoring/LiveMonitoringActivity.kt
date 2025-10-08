package com.automa.ui.live_monitoring

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.core.DataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.live_monitoring.model.LiveMonitoringModel
import com.automa.ui.R
import com.automa.ui.databinding.ActivityLiveMonitoringBinding
import com.automa.ui.databinding.LayoutMarkerInfoWindowBinding
import com.automa.ui.shared.auth.LoginActivity
import com.automa.ui.utils.DateUtils
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.MapUtils.bitmapDescriptorFromVector
import com.automa.ui.utils.formatNumberWithThousandSeparator
import com.automa.ui.utils.isNotDefaultData
import com.automa.ui.utils.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class LiveMonitoringActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityLiveMonitoringBinding

    private lateinit var gMap: GoogleMap

    private val liveMonitoringViewModel: LiveMonitoringViewModel by viewModels()
    private val listFleetMarkers: MutableList<Marker> = mutableListOf()
    private var isMoveCameraToFleetInitialized = false
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private val autoRefresh = Runnable {
        liveMonitoringViewModel.getLiveMonitoring()
    }
    private val timerAutoRefresh: Long = 90*1000
    private lateinit var countdownTimer: CountDownTimer
    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(binding.clListFleet)
    }

    @Inject lateinit var userData: DataStore<UserDataModel>

    private val liveMonitoringAdapter = LiveMonitoringAdapter(action = object : ItemLiveMonitoringClickListener {
        override fun onClicked(item: LiveMonitoringModel) {
            if (item.lat.isNotDefaultData() && item.lng.isNotDefaultData()) {
                val latLng = LatLng(item.lat, item.lng)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                listFleetMarkers.forEach {
                    if (it.position == latLng) {
                        it.showInfoWindow()
                    }
                }
            }
        }

        override fun onOpenMap(item: LiveMonitoringModel) {
            val uri = "geo:${item.lat},${item.lng}?q=${item.lat},${item.lng}(Fleet ${item.regNumberWithDoor})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                toast(this@LiveMonitoringActivity, "Google Maps is not installed")
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        initRv()
        initUi()
        initActions()
        binding.customMapView.onCreate(savedInstanceState)
        binding.customMapView.onResume()
        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.customMapView.getMapAsync(this)
    }

    private fun initActions() {
        binding.edtSearch.doAfterTextChanged {
            val result = liveMonitoringAdapter.filter(it.toString())
            populateMarkers(result)
        }
        binding.btnRefresh.setOnClickListener {
            liveMonitoringViewModel.getLiveMonitoring()
            it.isEnabled = false

            countdownTimer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    binding.btnRefresh.text = String.format("($secondsRemaining)")
                    updateButtonWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                }

                override fun onFinish() {
                    it.isEnabled = true
                    binding.btnRefresh.text = ""
                    updateButtonWidth((resources.displayMetrics.density*48).roundToInt())
                }
            }

            countdownTimer.start()
        }

        binding.btnLogout.setOnClickListener {
            DialogUtils.showWhiteAlertDialog(this@LiveMonitoringActivity, getString(R.string.title_logout),
                positiveButton = Pair(getString(R.string.ok)) {
                    runBlocking {
                        userData.updateData {
                            UserDataModel()
                        }
                        startActivity(Intent(this@LiveMonitoringActivity, LoginActivity::class.java))
                        finish()
                    }
                }, negativeButton = Pair(getString(R.string.cancel)) {

                }
            )
        }
    }

    private fun updateButtonWidth(newWidth: Int) {
        val params = binding.btnRefresh.layoutParams as ViewGroup.MarginLayoutParams
        if (params.width != newWidth) {
            params.width = newWidth
            binding.btnRefresh.layoutParams = params
        }
    }

    private fun initRv() {
        binding.rvLiveMonitoring.apply {
            layoutManager = LinearLayoutManager(this@LiveMonitoringActivity, LinearLayoutManager.VERTICAL, false)
            isMotionEventSplittingEnabled = false
            adapter = liveMonitoringAdapter
        }
    }

    private fun initUi() {
        bottomSheetBehavior.peekHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, Resources.getSystem().displayMetrics).toInt()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Handle state changes here
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val opacity = 1 - slideOffset
                binding.btnRefresh.alpha = opacity
            }
        })
    }

    private fun initObserver() {
        liveMonitoringViewModel.liveMonitoring.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    liveMonitoringAdapter.setData(success.data)
                    populateMarkers(success.data)
                    resetAutoRefresh()
                }, onFailure = { error ->
                    if (error.errorData.message.equals("Auth failed", ignoreCase = true)) {
                        DialogUtils.showWhiteAlertDialog(this@LiveMonitoringActivity,
                            "Session Expired",
                            "Please login again to continue",
                            positiveButton = Pair(getString(R.string.ok)) {
                                runBlocking {
                                    userData.updateData {
                                        UserDataModel()
                                    }
                                    startActivity(Intent(this@LiveMonitoringActivity, LoginActivity::class.java))
                                    finish()
                                }
                            }
                        )
                    } else {
                        toast(this@LiveMonitoringActivity, error.errorData.message ?: "Terjadi Kesalahan")
                        resetAutoRefresh()
                    }
                }, onLoading = {

                }
            )
        }
    }

    private val iconTruck by lazy { ContextCompat.getDrawable(this@LiveMonitoringActivity, R.drawable.ic_location_truck) }
    private val iconTruckOff by lazy { ContextCompat.getDrawable(this@LiveMonitoringActivity, R.drawable.ic_truck_off) }
    private val iconTruckAlert by lazy { ContextCompat.getDrawable(this@LiveMonitoringActivity, R.drawable.ic_truck_alert) }
    private val iconTruckIdle by lazy { ContextCompat.getDrawable(this@LiveMonitoringActivity, R.drawable.ic_truck_idle) }
    private val iconTruckMoving by lazy { ContextCompat.getDrawable(this@LiveMonitoringActivity, R.drawable.ic_truck_moving) }
    private val iconSize by lazy { (resources.displayMetrics.density*32).roundToInt() }
    private fun populateMarkers(list: List<LiveMonitoringModel>) {
        listFleetMarkers.forEach {  marker ->
            marker.remove()
        }
        list.filter { item -> item.lat.isNotDefaultData() && item.lng.isNotDefaultData() }
            .forEach { itemData ->
                val latLng = LatLng(itemData.lat, itemData.lng)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title(itemData.regNumberWithDoor)
                markerOptions.snippet(Gson().toJson(itemData))
                val iconToUse = if (itemData.wrn.isNotDefaultData() && itemData.spd.isNotDefaultData()) {
                    when {
                        itemData.wrn == 0 && itemData.spd > 0 -> iconTruckAlert
                        itemData.wrn == 0 -> iconTruckOff
                        itemData.wrn == 1 && itemData.spd > 0 -> iconTruckMoving
                        itemData.wrn == 1 && itemData.spd == 0 -> iconTruckIdle
                        else -> iconTruck
                    }
                } else {
                    iconTruck
                }
                markerOptions.icon(bitmapDescriptorFromVector(iconToUse, iconSize, iconSize))
                val marker = gMap.addMarker(markerOptions)
                if (marker != null) {
                    listFleetMarkers.add(marker)
                }
                if (!isMoveCameraToFleetInitialized) {
                    isMoveCameraToFleetInitialized = true
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
            }
    }

    private fun getTruckStatus(wrn: Int, spd: Int): String {
        return if (wrn.isNotDefaultData() && spd.isNotDefaultData()) {
            when {
                wrn == 0 && spd > 0 -> "Off (Warning!)"
                wrn == 0 -> "Off"
                wrn == 1 && spd > 0 -> "On & Moving"
                wrn == 1 && spd == 0 -> "On & Idle"
                else -> "-"
            }
        } else {
            "-"
        }
    }

    private fun resetAutoRefresh() {
        mHandler.removeCallbacks(autoRefresh)
        mHandler.postDelayed(autoRefresh, timerAutoRefresh)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        liveMonitoringViewModel.getLiveMonitoring()
        gMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                val binding = LayoutMarkerInfoWindowBinding.inflate(LayoutInflater.from(this@LiveMonitoringActivity))
                val itemData = Gson().fromJson(marker.snippet, LiveMonitoringModel::class.java)

                binding.tvIdFleetPopup.text = itemData.regNumberWithDoor
                binding.tvObd.text = itemData.obdCode
                binding.tvLastUpdated.text = DateUtils.formatDate(itemData.lastUpdateWithSpeed, DateUtils.DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm")

                if (itemData.fuelLevel.isNotDefaultData() && itemData.fuelTankCapacity.isNotDefaultData()) {
                    val currentFuel = (itemData.fuelLevel/100)*itemData.fuelTankCapacity
                    binding.tvFuelLevel.text = String.format("$currentFuel L /${itemData.fuelTankCapacity} L")
                } else {
                    binding.tvFuelLevel.text = "-"
                }

                //Speed
                if (itemData.spd.isNotDefaultData()) {
                    binding.tvSpeed.text = String.format("${itemData.spd} km/h")
                } else {
                    binding.tvSpeed.text = "-"
                }

                //Odometer
                if (itemData.odometer.isNotDefaultData()) {
                    binding.tvOdometer.text = String.format("${formatNumberWithThousandSeparator(itemData.odometer.div(1000.0))} km")
                } else {
                    binding.tvOdometer.text = "-"
                }

                //Position
                if (itemData.lat.isNotDefaultData() && itemData.lng.isNotDefaultData()) {
                    binding.tvPosition.text = String.format("${itemData.lat}, ${itemData.lng}")
                } else {
                    binding.tvPosition.text = "-"
                }
                binding.tvStatus.text = getTruckStatus(itemData.wrn, itemData.spd)

                return binding.root
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.customMapView.onResume()
        mHandler.removeCallbacks(autoRefresh)
        mHandler.postDelayed(autoRefresh, timerAutoRefresh)
    }

    override fun onPause() {
        super.onPause()
        binding.customMapView.onPause()
        mHandler.removeCallbacks(autoRefresh)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.customMapView.onDestroy()
        mHandler.removeCallbacks(autoRefresh)
        if (this::countdownTimer.isInitialized) {
            countdownTimer.cancel()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.customMapView.onLowMemory()
    }
}