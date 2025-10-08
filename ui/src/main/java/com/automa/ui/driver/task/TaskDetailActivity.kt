package com.automa.ui.driver.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.domain.common.ResultWrapper.Companion.handleResult
import com.automa.domain.driver_task.model.DeliveryOrderModel
import com.automa.domain.driver_task.model.TaskDetailModel
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.custom_component.AccordionTaskDetail
import com.automa.ui.databinding.ActivityTaskDetailBinding
import com.automa.ui.driver.task.pod.UploadPhotoBottomSheetFragment
import com.automa.ui.driver.task.pod.ViewPodActivity
import com.automa.ui.mechanic.breakdown_report.BreakdownReportActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.MapUtils
import com.automa.ui.utils.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.ncorti.slidetoact.SlideToActView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class TaskDetailActivity : BaseActivity(), OnMapReadyCallback {
    companion object {
        private const val DELIVERY_DATA = "DELIVERY_DATA"
        fun newIntent(context: Context): Intent {
            return Intent(context, TaskDetailActivity::class.java)
        }

        fun newIntent(context: Context, data: DeliveryOrderModel): Intent {
            return Intent(context, TaskDetailActivity::class.java).apply {
                putExtra(DELIVERY_DATA, data)
            }
        }
    }

    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var gMap: GoogleMap
    @Inject lateinit var deliveryOrderData: DataStore<UserDataModel>
    private var idDeliveryOrder = -1

    private var listMarker = mutableListOf<Marker>()

    private val taskDetailViewModel: TaskDetailViewModel by viewModels()

    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private lateinit var autoRefresh: Runnable
    private val timerAutoRefresh: Long = 7*60*1000
    private var isVehicleLocationAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initObserver()

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.mapView.getMapAsync(this)

        initActions()
    }

    private fun initData() {
        val deliveryData: DeliveryOrderModel? = intent.getParcelableExtra(DELIVERY_DATA)
        deliveryData?.let {
            binding.tvDONumber.text = it.deliveryOrderNumber
            binding.tvCurrentDeliveryTask.text = getString(R.string.message_current_task, it.deliveryOrderNumber)
            idDeliveryOrder = it.id
            binding.sliderTaskDone.isGone = true
        } ?: run {
            lifecycleScope.launch {
                deliveryOrderData.data.collect { userData ->
                    val data = userData.currentDeliveryOrder
                    if (data!=null) {
                        binding.tvDONumber.text = data.deliveryOrderNumber
//                    binding.tvEstArrivalAt.text = getString(R.string.text_arrival_time, "10.34")
                        binding.tvCurrentDeliveryTask.text = getString(R.string.message_current_task, data.deliveryOrderNumber)
                        idDeliveryOrder = data.id
                        autoRefresh = Runnable {
                            taskDetailViewModel.getTaskDetailRefresh(idDeliveryOrder)
                        }
                        mHandler.removeCallbacks(autoRefresh)
                        mHandler.postDelayed(autoRefresh, timerAutoRefresh)
                    }
                }
            }
        }
    }

    private fun initActions() {
        binding.swipeRefreshTaskDetailDriver.setOnRefreshListener {
            if (this::gMap.isInitialized) {
                binding.swipeRefreshTaskDetailDriver.isRefreshing = true
                taskDetailViewModel.getTaskDetail(idDeliveryOrder)
            } else {
                binding.swipeRefreshTaskDetailDriver.isRefreshing = false
            }
        }
        binding.btnToGmaps.setOnClickListener {
            if (this::gMap.isInitialized) {
                gMap.animateCamera(CameraUpdateFactory.zoomBy(1f))
            }
        }

        binding.btnZoomIn.setOnClickListener {
            if (this::gMap.isInitialized) {
                gMap.animateCamera(CameraUpdateFactory.zoomBy(1f))
            }
        }

        binding.btnZoomOut.setOnClickListener {
            if (this::gMap.isInitialized) {
                gMap.animateCamera(CameraUpdateFactory.zoomBy(-1f))
            }
        }

        binding.btnWarning.setOnClickListener {
            startActivity(Intent(this, BreakdownReportActivity::class.java))
        }

        binding.toolbarTaskDetail.setOnNavigationClickListener { finish() }

        binding.sliderTaskDone.onSlideCompleteListener = object: SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                taskDetailViewModel.checkTaskDetailCompletion(idDeliveryOrder)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        taskDetailViewModel.getTaskDetail(idDeliveryOrder)
    }

    private fun initObserver() {
        taskDetailViewModel.taskDetail.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    binding.swipeRefreshTaskDetailDriver.isRefreshing = false
                    if (success.data.checkIn.isNotEmpty()) {
                        binding.tvEstArrivalAt.text = getString(R.string.text_estimated_trip_until, success.data.checkIn.last().workOrderFrom.estArrivalTime)
                        binding.btnUploadPod.isEnabled = true
                        binding.btnUploadPod.setOnClickListener {
                            UploadPhotoBottomSheetFragment.newInstanceUploadPod(success.data.checkIn.first().workOrderFrom.id, success.data.checkIn.first().workOrderFrom.woNumber).show(supportFragmentManager, "")
                        }
                        binding.btnViewPod.setOnClickListener {
                            startActivity(ViewPodActivity.newIntent(this, success.data))
                        }
                    }
                    updateMap(success.data)
                    if (success.data.checkInResult.length>=3) {
                        binding.tvTotalCheckin.text = success.data.checkInResult.take(3)
                        binding.tvTotalPod.text = "0/${success.data.checkIn.count()}"
                    }
                }, onFailure = { error ->
                    toast(this@TaskDetailActivity, error.errorData.message ?: "Terjadi Kesalahan")
                    binding.swipeRefreshTaskDetailDriver.isRefreshing = false
                    Log.d("TAG-TASKDETAIL", "initObserver: Error->${error.errorData}")
                }
            )
        }

        taskDetailViewModel.taskDetailRefresh.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    updateMap(success.data)
                    if (success.data.checkInResult.length>=3) {
                        binding.tvTotalCheckin.text = success.data.checkInResult.take(3)
                    }
                    if (this::autoRefresh.isInitialized) {
                        mHandler.removeCallbacks(autoRefresh)
                        mHandler.postDelayed(autoRefresh, timerAutoRefresh)
                    }
                }, onFailure = {
                    if (this::autoRefresh.isInitialized) {
                        mHandler.removeCallbacks(autoRefresh)
                        mHandler.postDelayed(autoRefresh, timerAutoRefresh)
                    }
                }
            )
        }

        taskDetailViewModel.checkCompletion.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    loadingDialog.dismiss()
                    val totalCheckIn = success.data.checkIn.count()
                    var checkIns = 0

                    success.data.checkIn.forEach { checkIn ->
                        if (checkIn.idCheckIn != "0") checkIns++
                    }

                    if (checkIns == totalCheckIn) {
                        taskDetailViewModel.postTaskDone(idDeliveryOrder)
                    } else {
                        val message = getString(R.string.message_confirmation_complete_task, checkIns.toString())
                        DialogUtils.showWhiteAlertDialog(this@TaskDetailActivity, "", message,
                            positiveButton = Pair(getString(R.string.ok)) {
                                taskDetailViewModel.postTaskDone(idDeliveryOrder)
                            }, negativeButton = Pair(getString(R.string.cancel)) {
                                binding.sliderTaskDone.setCompleted(false, withAnimation = true)
                            })
                    }
                }, onFailure = { error ->
                    loadingDialog.dismiss()
                    toast(this@TaskDetailActivity, error.errorData.message ?: "Terjadi Kesalahan")
                    binding.sliderTaskDone.setCompleted(false, withAnimation = true)
                }, onLoading = {
                    loadingDialog.show()
                }
            )
        }

        taskDetailViewModel.postTaskDone.observe(this) {
            it.handleResult(
                onSuccess = { success ->
                    loadingDialog.dismiss()
                    if (success.data == 1) {
                        DialogUtils.showDialogInfoWithImage(this@TaskDetailActivity, R.drawable.ic_checklist, "Berhasil", "",
                            positiveButton = Pair(getString(R.string.ok)) {
                                runBlocking {
                                    deliveryOrderData.updateData { d ->
                                        d.copy(
                                            currentDeliveryOrder = null,
                                            refreshHome = true
                                        )
                                    }
                                }
                                finish()
                            })
                    } else {
                        toast(this@TaskDetailActivity, "Gagal")
                        binding.sliderTaskDone.setCompleted(false, withAnimation = true)
                    }
                }, onFailure = { error ->
                    loadingDialog.dismiss()
                    toast(this@TaskDetailActivity, error.errorData.message ?: "Terjadi Kesalahan")
                    binding.sliderTaskDone.setCompleted(false, withAnimation = true)
                }, onLoading = {
                    loadingDialog.show()
                }
            )
        }
    }

    private fun updateMap(data: TaskDetailModel) {
        val iconTruck = ContextCompat.getDrawable(this@TaskDetailActivity, R.drawable.ic_location_truck)
        val locationSize = (resources.displayMetrics.density*32).roundToInt()
        val iconLocation = ContextCompat.getDrawable(this@TaskDetailActivity, R.drawable.ic_location).apply {
            this?.mutate()
            this?.setTint(ContextCompat.getColor(this@TaskDetailActivity, R.color.black))
        }
        val iconFirstLocation = ContextCompat.getDrawable(this@TaskDetailActivity, R.drawable.ic_location)
        val iconLastLocation = ContextCompat.getDrawable(this@TaskDetailActivity, R.drawable.ic_location).apply {
            this?.mutate()
            this?.setTint(ContextCompat.getColor(this@TaskDetailActivity, R.color.success700))
        }
        listMarker.forEach { marker ->
            marker.remove()
        }
        listMarker.clear()
        binding.llAccordionTaskDetail.removeAllViews()
        data.checkIn.forEach { checkIn ->
            val view = AccordionTaskDetail(this)
            view.setData(checkIn.workOrderFrom)
            view.setButtonUploadClick {
                UploadPhotoBottomSheetFragment.newInstanceUploadPod(checkIn.workOrderFrom.id, checkIn.workOrderFrom.woNumber).show(supportFragmentManager, "")
            }
            binding.llAccordionTaskDetail.addView(view)
        }
        if (data.fleetStatus.lat != 0.0 && data.fleetStatus.lon != 0.0) {
            val latLng = LatLng(data.fleetStatus.lat, data.fleetStatus.lon)
            gMap.addMarker(MarkerOptions().apply {
                icon(MapUtils.bitmapDescriptorFromVector(iconTruck, locationSize, locationSize))
                position(latLng)
            }).also { m -> if (m!=null) {
                listMarker.add(m)
                isVehicleLocationAvailable = true
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            } }
        } else {
            isVehicleLocationAvailable = false
        }
        data.locationList.forEachIndexed { index, location ->
            val latLng = LatLng(location.lat, location.lng)
            try {
                val iconToShow = when (index) {
                    0 -> iconFirstLocation
                    data.locationList.size-1 -> iconLastLocation
                    else -> iconLocation
                }
                gMap.addMarker(MarkerOptions().apply {
                    icon(MapUtils.bitmapDescriptorFromVector(iconToShow, locationSize, locationSize))
                    position(latLng)
                }).also { m -> if (m!=null) listMarker.add(m) }
                if (index==0 && !isVehicleLocationAvailable) {
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
            } catch (e: Exception) {
                Log.d("TAG-MAP", "initObserver: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
        try {
            val polyline = PolyUtil.decode(data.overviewPolyline)
            gMap.addPolyline(PolylineOptions().apply {
                color(ContextCompat.getColor(this@TaskDetailActivity, R.color.neutral50))
                width(28f)
                addAll(polyline)
            })
            gMap.addPolyline(PolylineOptions().apply {
                color(ContextCompat.getColor(this@TaskDetailActivity, R.color.primary500))
                width(14f)
                addAll(polyline)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        if (this::autoRefresh.isInitialized) {
            mHandler.removeCallbacks(autoRefresh)
            mHandler.postDelayed(autoRefresh, timerAutoRefresh)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
        if (this::autoRefresh.isInitialized) {
            mHandler.removeCallbacks(autoRefresh)
        }
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