import 'package:drivero_automa/domain/usecases/add_maintenance_task_use_case.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:drivero_automa/domain/usecases/get_maintenances_usecase.dart';
import 'package:drivero_automa/domain/usecases/get_fleets_usecase.dart';
import 'package:drivero_automa/presentation/schedule_history/models/driver_fleet_model.dart';
import 'package:drivero_automa/presentation/schedule_history/models/task_item.dart';
import 'task_detail_event.dart';
import 'task_detail_state.dart';

class EventBloc extends Bloc<TaskDetailEvent, TaskState> {
  final GetMaintenancesUsecase getMaintenancesUsecase;
  final GetDriverFleets getDriverFleets;
  final AddMaintenanceTaskUseCase addMaintenanceTaskUseCase;

  List<TaskItem> allTasks = [];
  List<DriverFleetModel> allFleets = [];

  EventBloc({
    required this.getMaintenancesUsecase,
    required this.getDriverFleets,
    required this.addMaintenanceTaskUseCase,
  }) : super(const TaskInitial()) {
    // --------------------------------------------------------------------
    // 1. LOAD FLEETS
    // --------------------------------------------------------------------
    on<LoadFleets>((event, emit) async {
      emit(const TaskLoading());

      final fleetsResult = await getDriverFleets(event.userId);

      fleetsResult.fold((failure) => emit(TaskError(failure)), (fleetList) {
        final fleetListUI = fleetList
            .map(
              (fleet) =>
                  DriverFleetModel(id: fleet.vehicleId, name: fleet.brand),
            )
            .toList();

        allFleets = fleetListUI;

        emit(FleetLoaded(fleetListUI));
      });
    });

    // --------------------------------------------------------------------
    // 2. LOAD TASKS / MAINTENANCES
    // --------------------------------------------------------------------
    on<LoadTasks>((event, emit) async {
      emit(const TaskLoading());

      final result = await getMaintenancesUsecase();
      result.fold((failure) => emit(TaskError(failure)), (data) {
        // Sort berdasar tanggal terbaru
        data.sort(
          (a, b) => (b.dateTimes ?? DateTime(0))
              .compareTo(a.dateTimes ?? DateTime(0)),
        );

        // Mapping vehicleId -> nama kendaraan
        final Map<int, String> vehicleMap = {
          for (var v in allFleets) v.id: v.name,
        };

        allTasks = data
            .map(
              (e) => TaskItem(
                title: e.title,
                date: e.dateTimes ?? DateTime.now(),
                description: e.description,
                car: vehicleMap[e.vehicleId] ?? e.vehicleId.toString(),
                status: e.enumStatus ?? "",
              ),
            )
            .toList();

        emit(TaskLoaded(allTasks));
      });
    });

    // --------------------------------------------------------------------
    // 3. FILTER TASK (ALL / RECENT TASK)
    // --------------------------------------------------------------------
    on<FilterTasks>((event, emit) {
      if (state is! TaskLoaded) return;

      List<TaskItem> filtered = [];
      DateTime now = DateTime.now();

      if (event.status == "All") {
        filtered = allTasks;
      }

      // RECENT = bulan & tahun yang sama dengan hari ini
      else if (event.status == "Recent Task") {
        filtered = allTasks.where((t) {
          return t.date.month == now.month &&
              t.date.year == now.year;
        }).toList()
          ..sort((a, b) => b.date.compareTo(a.date));
      }

      emit(TaskLoaded(filtered));
    });

    // --------------------------------------------------------------------
    // 4. FILTER TANGGAL DARI KALENDER
    // --------------------------------------------------------------------
    on<FilterByDate>((event, emit) {
      if (state is! TaskLoaded) return;

      List<TaskItem> filtered = allTasks.where((task) {
        return task.date.year == event.selectedDate.year &&
            task.date.month == event.selectedDate.month &&
            task.date.day == event.selectedDate.day;
      }).toList()
        ..sort((a, b) => b.date.compareTo(a.date));

      emit(TaskLoaded(filtered));
    });

    // --------------------------------------------------------------------
    // 5. ADD NEW TASK
    // --------------------------------------------------------------------
    on<AddMaintenanceTask>((event, emit) async {
      emit(const TaskLoading());

      final result = await addMaintenanceTaskUseCase.call(
        vehicleId: event.vehicleId,
        title: event.title,
        maintenanceType: event.maintenanceType,
        location: event.location,
        description: event.description,
        feeString: event.feeString,
        dateTimes: event.dateTimes,
        enumStatus: event.enumStatus,
      );

      result.fold((failure) => emit(TaskError(failure.toString())), (_) {
        final vehicleName = allFleets
            .firstWhere(
              (v) => v.id == event.vehicleId,
              orElse: () => DriverFleetModel(
                id: event.vehicleId,
                name: event.vehicleId.toString(),
              ),
            )
            .name;

        final newTask = TaskItem(
          title: event.title,
          date: DateTime.tryParse(event.dateTimes) ?? DateTime.now(),
          description: event.description,
          car: vehicleName,
          status: event.enumStatus,
        );

        allTasks.add(newTask);
        emit(TaskAdded(newTask));
      });
    });
  }
}
