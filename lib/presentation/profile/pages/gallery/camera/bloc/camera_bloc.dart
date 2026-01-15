import 'dart:io';
import 'dart:typed_data';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:geolocator/geolocator.dart';
import 'package:geocoding/geocoding.dart';
import 'package:path_provider/path_provider.dart';
import 'package:uuid/uuid.dart';
import 'package:intl/intl.dart';
import 'package:image/image.dart' as img; // Library manipulasi gambar

import 'camera_event.dart';
import 'camera_state.dart';

class CameraBloc extends Bloc<CameraEvent, CameraState> {
  final Uuid _uuid = const Uuid();

  CameraBloc() : super(CameraInitial()) {
    on<InitCameraLocation>(_onInitLocation);
    on<SaveCapturedPhoto>(_onSavePhoto);
  }

  // --- Logic 1: Lokasi (Tidak Berubah) ---
  Future<void> _onInitLocation(
      InitCameraLocation event, Emitter<CameraState> emit) async {
    emit(const CameraLocationUpdated("Mencari lokasi..."));
    try {
      bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) {
        emit(const CameraLocationUpdated("GPS Tidak Aktif"));
        return;
      }
      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) {
          emit(const CameraLocationUpdated("Izin Lokasi Ditolak"));
          return;
        }
      }

      Position position = await Geolocator.getCurrentPosition(
          desiredAccuracy: LocationAccuracy.high);

      List<Placemark> placemarks = await placemarkFromCoordinates(
          position.latitude, position.longitude);

      Placemark place = placemarks[0];
      String address =
          "${place.street ?? ''}, ${place.subLocality ?? ''}, ${place.locality ?? ''}";

      emit(CameraLocationUpdated(address));
    } catch (e) {
      emit(const CameraLocationUpdated("Gagal mendapatkan lokasi"));
    }
  }

  // --- Logic 2: Simpan Foto + Watermark (BARU) ---
  Future<void> _onSavePhoto(
      SaveCapturedPhoto event, Emitter<CameraState> emit) async {
    emit(CameraSaveLoading());

    try {
      // 1. Generate ID Unik & Tanggal
      final String photoId = _uuid.v4().substring(0, 8).toUpperCase(); // ID Singkat (8 digit)
      final String timestamp = DateFormat('yyyy-MM-dd HH:mm:ss').format(DateTime.now());
      final String watermarkText = "ID: $photoId | $timestamp\nDRIVERO by Automa";

      // 2. Baca Gambar dari File Sementara
      final File originalFile = File(event.image.path);
      final List<int> imageBytes = await originalFile.readAsBytes();
      final img.Image? decodedImage = img.decodeImage(Uint8List.fromList(imageBytes));

      if (decodedImage == null) throw Exception("Gagal decode gambar");

      // 3. Tambahkan Watermark
      // Menggunakan font bawaan library (arial48 ukuran cukup besar)
      img.drawString(
        decodedImage,
        watermarkText,
        font: img.arial48, 
        x: 20,
        y: decodedImage.height - 120, // Posisi di Bawah Kiri
        color: img.ColorRgb8(255, 255, 0), // Warna Kuning agar kontras
      );

      // 4. Simpan ke Storage Lokal
      final dir = await getApplicationDocumentsDirectory();
      String cleanName = event.customName.trim();
      if (!cleanName.toLowerCase().endsWith('.jpg')) {
        cleanName = "$cleanName.jpg";
      }

      final newPath = '${dir.path}/$photoId-$cleanName';
      final File newFile = File(newPath);

      // Encode kembali ke JPG dan tulis ke file
      await newFile.writeAsBytes(img.encodeJpg(decodedImage, quality: 85));

      emit(CameraSaveSuccess(newPath));
    } catch (e) {
      emit(CameraFailure("Gagal memproses foto: $e"));
    }
  }
}