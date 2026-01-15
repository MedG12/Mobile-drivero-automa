import 'package:equatable/equatable.dart';

sealed class LastMaintenanceEvent extends Equatable {
  @override
  List<Object?> get props => [];
}

class GetLastMaintenanceEvent extends LastMaintenanceEvent {}
