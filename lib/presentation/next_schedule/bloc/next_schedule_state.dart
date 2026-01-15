import 'package:equatable/equatable.dart';
import 'package:drivero_automa/domain/entities/maintenance_entity.dart';

class NextScheduleState extends Equatable {
  final List<MaintenanceEntity> upcoming;
  final List<MaintenanceEntity> completed;

  final bool isLoading;
  final String? error;

  const NextScheduleState({
    this.upcoming = const [],
    this.completed = const [],
    this.isLoading = false,
    this.error,
  });

  factory NextScheduleState.initial() {
    return const NextScheduleState(
      upcoming: [],
      completed: [],
      isLoading: false,
      error: null,
    );
  }

  NextScheduleState copyWith({
    List<MaintenanceEntity>? upcoming,
    List<MaintenanceEntity>? completed,
    bool? isLoading,
    String? error,
  }) {
    return NextScheduleState(
      upcoming: upcoming ?? this.upcoming,
      completed: completed ?? this.completed,
      isLoading: isLoading ?? this.isLoading,
      error: error,
    );
  }

  @override
  List<Object?> get props => [
        upcoming,
        completed,
        isLoading,
        error,
      ];
}
