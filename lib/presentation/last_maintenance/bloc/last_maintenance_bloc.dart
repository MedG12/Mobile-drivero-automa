import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_event.dart';
import 'package:drivero_automa/presentation/last_maintenance/bloc/last_maintenance_state.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class LastMaintenanceBloc
    extends Bloc<LastMaintenanceEvent, LastMaintenanceState> {
  final GetMaintenancesUsecase getMaintenances;

  LastMaintenanceBloc({required this.getMaintenances})
      : super(LastMaintenanceInitial()) {
    
    on<GetLastMaintenanceEvent>((event, emit) async {
      // 1. Emit Loading State
      emit(LastMaintenanceLoading());

      try {
        // 2. Panggil Usecase
        final result = await getMaintenances();

        result.fold(
          (failure) {
            // 3. Handle jika API Error
            emit(LastMaintenanceError(message: failure.toString()));
          },
          (data) {
            // 4. FILTER: Hanya ambil status "Completed"
            final completedList = data
                .where((m) => m.enumStatus == "Completed")
                .toList();

            // 5. SORTING: Urutkan dari Tanggal Paling Baru ke Lama (Descending)
            // Logic ini penting agar "Last Maintenance" benar-benar yang terakhir
            if (completedList.isNotEmpty) {
              completedList.sort((a, b) {
                // Safety: Jika modifiedOn null, anggap tahun 2000 (paling bawah)
                final dateA = a.modifiedOn ?? DateTime(2000);
                final dateB = b.modifiedOn ?? DateTime(2000);
                
                // Compare B ke A (Descending)
                return dateB.compareTo(dateA); 
              });
            }

            // 6. Emit Data Bersih & Terurut
            emit(
              LastMaintenanceLoaded(
                lastMaintenances: completedList,
              ),
            );
          },
        );
      } catch (e) {
        // 7. Safety Net: Tangkap error tak terduga (misal parsing error)
        emit(LastMaintenanceError(message: e.toString()));
      }
    });
  }
}