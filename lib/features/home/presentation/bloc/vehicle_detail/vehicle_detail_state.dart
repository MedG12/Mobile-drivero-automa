part of 'vehicle_detail_bloc.dart';

sealed class VehicleDetailState extends Equatable {
  const VehicleDetailState();
  
  @override
  List<Object> get props => [];
}

final class VehicleDetailInitial extends VehicleDetailState {}
