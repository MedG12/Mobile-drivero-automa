import 'package:drivero_automa/features/schedule_history/presentation/models/task_item.dart';
import 'package:equatable/equatable.dart';

sealed class TaskState extends Equatable {
  const TaskState();

  @override
  List<Object?> get props => [];
}

class TaskInitial extends TaskState {
  const TaskInitial();
}

class TaskLoading extends TaskState {
  const TaskLoading();
}

class TaskLoaded extends TaskState {
  final List<TaskItem> taskDetails;

  const TaskLoaded(this.taskDetails);

  @override
  List<Object?> get props => [taskDetails];
}

class TaskError extends TaskState {
  final String message;

  const TaskError(this.message);
}

class TaskEmpty extends TaskState {}
