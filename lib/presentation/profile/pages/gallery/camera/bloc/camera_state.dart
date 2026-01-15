import 'package:equatable/equatable.dart';

abstract class CameraState extends Equatable {
  const CameraState();
  
  @override
  List<Object> get props => [];
}

// State Awal
class CameraInitial extends CameraState {}

// State untuk update UI Lokasi (GPS)
class CameraLocationUpdated extends CameraState {
  final String address; // Alamat atau pesan error GPS (misal: "GPS Mati")
  
  const CameraLocationUpdated(this.address);
  
  @override
  List<Object> get props => [address];
}

// State saat proses menyimpan foto berlangsung (bisa untuk show loading dialog)
class CameraSaveLoading extends CameraState {}

// State jika foto BERHASIL disimpan
class CameraSaveSuccess extends CameraState {
  final String savedPath;
  
  const CameraSaveSuccess(this.savedPath);
  
  @override
  List<Object> get props => [savedPath];
}

// State jika GAGAL (GPS error parah atau Gagal Simpan)
class CameraFailure extends CameraState {
  final String message;
  
  const CameraFailure(this.message);
  
  @override
  List<Object> get props => [message];
}