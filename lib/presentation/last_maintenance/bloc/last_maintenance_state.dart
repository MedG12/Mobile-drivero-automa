import 'package:drivero_automa/domain/entities/maintenance_entity.dart';
import 'package:equatable/equatable.dart';

abstract class LastMaintenanceState extends Equatable {
  @override
  List<Object?> get props => [];
}

class LastMaintenanceInitial extends LastMaintenanceState {}

class LastMaintenanceLoading extends LastMaintenanceState {}

class LastMaintenanceError extends LastMaintenanceState {
  final String message;
  LastMaintenanceError({required this.message});

  // ⚠️ WAJIB DITAMBAHKAN: Biar kalau error berubah, UI update
  @override
  List<Object> get props => [message];
}

class LastMaintenanceLoaded extends LastMaintenanceState {
  final List<MaintenanceEntity> lastMaintenances;
  LastMaintenanceLoaded({required this.lastMaintenances});

  // ⚠️ SANGAT PENTING:
  // Tanpa ini, BlocBuilder menganggap state SAMA persis walau list berubah,
  // jadi UI gak bakal berubah.
  @override
  List<Object> get props => [lastMaintenances];
}