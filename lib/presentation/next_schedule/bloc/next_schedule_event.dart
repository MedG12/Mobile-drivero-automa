import 'package:equatable/equatable.dart';

sealed class NextScheduleEvent extends Equatable {
  const NextScheduleEvent();

  @override
  List<Object?> get props => [];
}

// Hanya satu event: Load semua data maintenance
class LoadNextScheduleEvent extends NextScheduleEvent {}
