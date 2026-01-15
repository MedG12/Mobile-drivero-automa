import 'package:equatable/equatable.dart';

sealed class TaskDetailEvent extends Equatable {
  const TaskDetailEvent();
}
// Event untuk load semua fleet (kendaraan)
class LoadFleets extends TaskDetailEvent {

  final int userId; 


  const LoadFleets({required this.userId}); 

  @override
  List<Object?> get props => [userId];
}


class LoadTasks extends TaskDetailEvent {
  const LoadTasks();

  @override
  List<Object?> get props => [];
}

class FilterTasks extends TaskDetailEvent {
  final String status; 

  const FilterTasks(this.status);

  @override
  List<Object?> get props => [status];
}


class AddMaintenanceTask extends TaskDetailEvent {
  final int vehicleId;
  final String title;
  final String maintenanceType;
  final String location;
  final String description;
  final String feeString;
  final String dateTimes;
  final String enumStatus;

  const AddMaintenanceTask({
    required this.vehicleId,
    required this.title,
    required this.maintenanceType,
    required this.location,
    required this.description,
    required this.feeString,
    required this.dateTimes,
    required this.enumStatus,
  });

  @override
  List<Object?> get props => [
        vehicleId,
        title,
        maintenanceType,
        location,
        description,
        feeString,
        dateTimes,
        enumStatus,
      ];
}
class FilterByDate extends TaskDetailEvent {
  final DateTime selectedDate;

  const FilterByDate(this.selectedDate);

  @override
  List<Object?> get props => [selectedDate];
}

