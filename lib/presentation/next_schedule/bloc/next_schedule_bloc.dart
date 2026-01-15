import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
import 'next_schedule_event.dart';
import 'next_schedule_state.dart';

class NextScheduleBloc extends Bloc<NextScheduleEvent, NextScheduleState> {
  final GetMaintenancesUsecase getMaintenances;

  NextScheduleBloc({required this.getMaintenances})
    : super(NextScheduleState.initial()) {
    on<LoadNextScheduleEvent>((event, emit) async {
      emit(state.copyWith(isLoading: true, error: null));

      // Panggil usecase (ambil semua data)
      final result = await getMaintenances();

      result.fold(
        (failure) {
          emit(state.copyWith(isLoading: false, error: failure));
        },
        (data) {
          emit(
            state.copyWith(
              isLoading: false,

              // Filter berdasarkan enum
              upcoming: data.where((m) => m.enumStatus == "Pending").toList(),
              completed: data
                  .where((m) => m.enumStatus == "Completed")
                  .toList(),
            ),
          );
        },
      );
    });
  }
}
