part of 'vehicle_detail_bloc.dart';

sealed class VehicleDetailEvent extends Equatable {
  const VehicleDetailEvent();

  @override
  List<Object> get props => [];
}

class GetVehicleDetailEvent extends VehicleDetailEvent {
  final int vehicleId;
  const GetVehicleDetailEvent({required this.vehicleId});
}
