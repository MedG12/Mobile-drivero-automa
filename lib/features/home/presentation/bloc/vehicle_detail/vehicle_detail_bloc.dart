import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';

part 'vehicle_detail_event.dart';
part 'vehicle_detail_state.dart';

class VehicleDetailBloc extends Bloc<VehicleDetailEvent, VehicleDetailState> {
  VehicleDetailBloc() : super(VehicleDetailInitial()) {
    on<VehicleDetailEvent>((event, emit) {
      // TODO: implement event handler
    });
  }
}
