import 'package:equatable/equatable.dart';
import '../models/task_item.dart';
import '../models/driver_fleet_model.dart';

sealed class TaskState extends Equatable {
  const TaskState();

  @override
  List<Object?> get props => [];
}

// State awal, sebelum ada aksi apapun
class TaskInitial extends TaskState {
  const TaskInitial();
}

// State loading, bisa dipakai untuk loading fleet atau maintenance
class TaskLoading extends TaskState {
  const TaskLoading();
}

// State ketika maintenance berhasil di-load
class TaskLoaded extends TaskState {
  final List<TaskItem> taskDetails;

  const TaskLoaded(this.taskDetails);

  @override
  List<Object?> get props => [taskDetails];
}

// State ketika fleet berhasil di-load
class FleetLoaded extends TaskState {
  final List<DriverFleetModel> fleets;

  const FleetLoaded(this.fleets);

  @override
  List<Object?> get props => [fleets];
}

// State gabungan jika mau sekaligus bawa fleet + task, bisa buat tambahan:
class TaskWithFleetLoaded extends TaskState {
  final List<TaskItem> taskDetails;
  final List<DriverFleetModel> fleets;

  const TaskWithFleetLoaded({
    required this.taskDetails,
    required this.fleets,
  });

  @override
  List<Object?> get props => [taskDetails, fleets];
}

// State kosong (misal tidak ada maintenance)
class TaskEmpty extends TaskState {
  const TaskEmpty();
}

// State error
class TaskError extends TaskState {
  final String message;

  const TaskError(this.message);

  @override
  List<Object?> get props => [message];
}
class TaskAdded extends TaskState {
  final TaskItem newTask; // optional, bisa bawa data yang baru ditambahkan

  const TaskAdded(this.newTask);

  @override
  List<Object?> get props => [newTask];
}