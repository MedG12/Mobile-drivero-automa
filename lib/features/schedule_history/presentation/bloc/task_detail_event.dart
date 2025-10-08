import 'package:equatable/equatable.dart';

sealed class TaskDetailEvent extends Equatable {}

class LoadTasks extends TaskDetailEvent {
  @override
  List<Object?> get props => [];
}

class FilterTasks extends TaskDetailEvent {
  final String status;

  FilterTasks(this.status);

  @override
  List<Object?> get props => [status];
}
