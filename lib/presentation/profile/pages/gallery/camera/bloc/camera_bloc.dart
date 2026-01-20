import 'dart:io';
import 'dart:typed_data';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:geolocator/geolocator.dart';
import 'package:geocoding/geocoding.dart';
import 'package:path_provider/path_provider.dart';
import 'package:uuid/uuid.dart';
import 'package:intl/intl.dart';
import 'package:image/image.dart' as img; 
// ✅ WAJIB: Import ini untuk request izin barengan
import 'package:permission_handler/permission_handler.dart'; 

import 'camera_event.dart';
import 'camera_state.dart';

class CameraBloc extends Bloc<CameraEvent, CameraState> {
  final Uuid _uuid = const Uuid();

  CameraBloc() : super(CameraInitial()) {
    on<InitCameraLocation>(_onInitLocation);
    on<SaveCapturedPhoto>(_onSavePhoto);
  }

  // --- Logic 1: Init Izin Barengan & Cari Lokasi ---
  Future<void> _onInitLocation(
      InitCameraLocation event, Emitter<CameraState> emit) async {
    
    emit(const CameraLocationUpdated("Meminta Izin..."));

    try {
      // 1. Request Izin Kamera & Lokasi SEKALIGUS
      // Array ini membuat popup sistem muncul berurutan
      Map<Permission, PermissionStatus> statuses = await [
        Permission.camera,
        Permission.location,
      ].request();

      final bool isCameraGranted = statuses[Permission.camera]!.isGranted;
      final bool isLocationGranted = statuses[Permission.location]!.isGranted;

      // 2. Validasi Izin
      if (isCameraGranted && isLocationGranted) {
        
        // Cek GPS Nyala/Mati
        bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
        if (!serviceEnabled) {
          emit(const CameraLocationUpdated("GPS Mati, mohon aktifkan"));
          return;
        }

        // 3. Ambil Titik Koordinat
        Position position = await Geolocator.getCurrentPosition(
            desiredAccuracy: LocationAccuracy.high);

        // 4. Ubah ke Alamat (Reverse Geocoding)
        List<Placemark> placemarks = await placemarkFromCoordinates(
            position.latitude, position.longitude);

        if (placemarks.isNotEmpty) {
          Placemark place = placemarks[0];
          
          // Ambil alamat yang cukup lengkap (Jalan, Kelurahan, Kecamatan)
          // Kita tidak perlu memotong paksa di sini, nanti diatur saat Save Photo
          String address = "${place.street ?? ''}, ${place.subLocality ?? ''}, ${place.locality ?? ''}";
          
          // Bersihkan koma berlebih jika ada data kosong
          address = address.replaceAll(RegExp(r'^, | , |,$'), '').trim();
          
          emit(CameraLocationUpdated(address));
        } else {
          emit(const CameraLocationUpdated("Lokasi tidak dikenali"));
        }

      } else {
        // Jika user menolak salah satu izin
        if (!isCameraGranted) {
          emit(const CameraFailure("Izin Kamera Ditolak! Aplikasi tidak bisa berjalan."));
        } else {
          emit(const CameraLocationUpdated("Izin Lokasi Ditolak (Watermark Kosong)"));
        }
      }
    } catch (e) {
      emit(CameraFailure("Error Lokasi: $e"));
    }
  }

  // --- Logic 2: Simpan Foto + Watermark (FONT KECIL & WRAPPING) ---
  Future<void> _onSavePhoto(
      SaveCapturedPhoto event, Emitter<CameraState> emit) async {
    emit(CameraSaveLoading());

    try {
      // 1. Siapkan Data Watermark
      final String photoId = _uuid.v4().substring(0, 8).toUpperCase();
      final String timestamp = DateFormat('dd MMM yyyy HH:mm:ss').format(DateTime.now());
      
      // Ambil teks lokasi dari Event
      String locationText = event.location;

      // 🔥 LOGIC BARU: Text Wrapping (Pindah Baris)
      // Jika lokasi lebih dari 45 karakter, kita potong di spasi terdekat dan enter (\n)
      if (locationText.length > 45) {
        int splitIndex = locationText.lastIndexOf(' ', 45); 
        if (splitIndex != -1) {
          String part1 = locationText.substring(0, splitIndex);
          String part2 = locationText.substring(splitIndex + 1);
          locationText = "$part1\n$part2"; // \n = Enter
        }
      }

      // Gabungkan semua teks
      final String watermarkText = 
          "ID: $photoId\n"
          "Time: $timestamp\n"
          "Loc: $locationText";

      // 2. Decode Gambar
      final File originalFile = File(event.image.path);
      final List<int> imageBytes = await originalFile.readAsBytes();
      final img.Image? decodedImage = img.decodeImage(Uint8List.fromList(imageBytes));

      if (decodedImage == null) throw Exception("Gagal decode gambar");

      // 3. Gambar Watermark (FONT KECIL)
      img.drawString(
        decodedImage,
        watermarkText,
        font: img.arial24, // ✅ Pakai Font Kecil (arial24)
        x: 20, 
        y: decodedImage.height - 150, // ✅ Margin Bawah disesuaikan (150px cukup)
        color: img.ColorRgb8(255, 255, 0), // Warna Kuning
      );

      // 4. Simpan File ke Storage
      final dir = await getApplicationDocumentsDirectory();
      String cleanName = event.customName.trim();
      if (!cleanName.toLowerCase().endsWith('.jpg')) {
        cleanName = "$cleanName.jpg";
      }

      final newPath = '${dir.path}/$photoId-$cleanName';
      final File newFile = File(newPath);

      // Encode kembali ke JPG dan tulis file
      await newFile.writeAsBytes(img.encodeJpg(decodedImage, quality: 85));

      emit(CameraSaveSuccess(newPath));
    } catch (e) {
      emit(CameraFailure("Gagal simpan foto: $e"));
    }
  }
}