import 'package:camera/camera.dart';
import 'package:equatable/equatable.dart';

abstract class CameraEvent extends Equatable {
  const CameraEvent();

  @override
  List<Object> get props => [];
}

// Event 1: Init Lokasi (Tetap)
class InitCameraLocation extends CameraEvent {}

// Event 2: Simpan Foto (DIUBAH)
class SaveCapturedPhoto extends CameraEvent {
  final XFile image;
  final String customName;
  final String location; // <--- TAMBAHAN: Untuk kirim teks lokasi ke Bloc

  const SaveCapturedPhoto({
    required this.image,
    required this.customName,
    required this.location, // <--- Wajib diisi dari UI
  });

  @override
  // Jangan lupa masukkan location ke props agar Equatable mendeteksi perubahan
  List<Object> get props => [image, customName, location];
}