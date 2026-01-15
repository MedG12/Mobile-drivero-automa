import 'package:camera/camera.dart';
import 'package:equatable/equatable.dart';

abstract class CameraEvent extends Equatable {
  const CameraEvent();

  @override
  List<Object> get props => [];
}

// Event 1: Panggil saat membuka layar kamera untuk cari lokasi GPS
class InitCameraLocation extends CameraEvent {}

// Event 2: Panggil saat user menekan tombol "Simpan" di dialog
class SaveCapturedPhoto extends CameraEvent {
  final XFile image;
  final String customName;

  const SaveCapturedPhoto({
    required this.image,
    required this.customName,
  });

  @override
  List<Object> get props => [image, customName];
}