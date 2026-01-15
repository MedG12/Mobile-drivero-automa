// import 'dart:async';
// import 'dart:io';
// import 'package:flutter/material.dart';
// import 'package:camera/camera.dart';
// import 'package:geolocator/geolocator.dart';
// import 'package:geocoding/geocoding.dart';
// import 'package:intl/intl.dart';
// import 'camera_dialog.dart';

// class CustomCameraController {
//   final CameraDescription camera;
//   final BuildContext context;
//   final VoidCallback onUpdate;

//   late CameraController cameraController;
//   late Future<void> initializeControllerFuture;
//   Timer? _timer;

//   String timestamp = "Memuat waktu...";
//   String locationMessage = "Mencari lokasi...";
//   bool showPreview = false;
//   bool isDialogShowing = false;
//   XFile? capturedImage;

//   CustomCameraController({
//     required this.camera,
//     required this.context,
//     required this.onUpdate,
//   });

//   void init() {
//     cameraController = CameraController(camera, ResolutionPreset.high);
//     initializeControllerFuture = cameraController.initialize().then((_) {
//       _initPermissionsAndUpdates();
//       onUpdate();
//     }).catchError((e) {
//       debugPrint("Error initializing camera: $e");
//     });
//   }

//   void disposeController() {
//     _timer?.cancel();
//     cameraController.dispose();
//   }

//   void _initPermissionsAndUpdates() {
//     _determinePosition();
//     _updateTimestamp();
//   }

//   void _updateTimestamp() {
//     _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
//       timestamp = DateFormat('yyyy-MM-dd HH:mm:ss').format(DateTime.now());
//       onUpdate();
//     });
//   }

//   Future<void> _determinePosition() async {
//     bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
//     if (!serviceEnabled) {
//       locationMessage = "GPS tidak aktif.";
//       onUpdate();
//       return;
//     }

//     LocationPermission permission = await Geolocator.checkPermission();
//     if (permission == LocationPermission.denied) {
//       permission = await Geolocator.requestPermission();
//       if (permission == LocationPermission.denied) {
//         locationMessage = "Izin lokasi ditolak.";
//         onUpdate();
//         return;
//       }
//     }

//     if (permission == LocationPermission.deniedForever) {
//       locationMessage = "Izin lokasi ditolak permanen.";
//       onUpdate();
//       return;
//     }

//     try {
//       Position position = await Geolocator.getCurrentPosition(
//         desiredAccuracy: LocationAccuracy.high,
//       );
//       List<Placemark> placemarks =
//           await placemarkFromCoordinates(position.latitude, position.longitude);
//       Placemark place = placemarks[0];
//       locationMessage =
//           "${place.street ?? ''}, ${place.subLocality ?? ''}, ${place.locality ?? ''}";
//     } catch (e) {
//       locationMessage = "Gagal mendapatkan alamat.";
//       debugPrint("Error geolocator: $e");
//     }
//     onUpdate();
//   }

//   Future<void> capturePhoto() async {
//     if (!cameraController.value.isInitialized) {
//       debugPrint("Error: Controller belum siap.");
//       return;
//     }

//     try {
//       await initializeControllerFuture;
//       final image = await cameraController.takePicture();
//       capturedImage = image;
//       showPreview = true;
//       onUpdate();

//       if (!context.mounted) return;
//       isDialogShowing = true;

//       await CameraDialog.showSaveDialog(
//         context,
//         image,
//         onSaved: () {
//           // Setelah simpan, tetap di halaman kamera tapi bisa trigger callback
//           showPreview = false;
//           capturedImage = null;
//           isDialogShowing = false;
//           onUpdate();
//         },
//         onCancel: () {
//           // Batal → kembali ke live camera
//           showPreview = false;
//           capturedImage = null;
//           isDialogShowing = false;
//           onUpdate();
//         },
//       );
//     } catch (e) {
//       debugPrint("Error saat mengambil foto: $e");
//       showPreview = false;
//       isDialogShowing = false;
//       onUpdate();
//     }
//   }
// }
