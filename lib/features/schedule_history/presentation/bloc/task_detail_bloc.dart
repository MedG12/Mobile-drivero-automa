import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_event.dart';
import 'package:drivero_automa/features/schedule_history/presentation/bloc/task_detail_state.dart';
import 'package:drivero_automa/features/schedule_history/presentation/models/task_item.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

final List<TaskItem> dummyTasks = [
  TaskItem(
    title: "Event Test Jan 2025",
    date: DateTime(2025, 1, 11, 17, 45),
    description: "Maintenance for Tesla Model X",
    car: "AB1234 BCF",
    status: "Ongoing",
  ),
  TaskItem(
    title: "Event Test Feb 2025",
    date: DateTime(2025, 2, 20, 14, 30),
    description: "Oil Change for Toyota Avanza",
    car: "B5678 XYZ",
    status: "Pending",
  ),
  TaskItem(
    title: "Event Test Mar 2025",
    date: DateTime(2025, 3, 5, 10, 0),
    description: "Brake Check for Honda Civic",
    car: "D9101 KLM",
    status: "Completed",
  ),
  TaskItem(
    title: "Event Test Apr 2025",
    date: DateTime(2025, 4, 15, 9, 15),
    description: "Tire Replacement for Suzuki Ertiga",
    car: "E2345 NOP",
    status: "Ongoing",
  ),
  TaskItem(
    title: "Event Test Dec 2024",
    date: DateTime(2024, 12, 12, 17, 45),
    description: "Battery Replacement for Mitsubishi Xpander",
    car: "F6789 QRS",
    status: "Pending",
  ),
];

class EventBloc extends Bloc<TaskDetailEvent, TaskState> {
  EventBloc() : super(const TaskInitial()) {
    on<LoadTasks>((event, emit) {
      // Handle the load tasks logic here
      // For demonstration, we'll just emit a TaskLoaded state with dummy data
      emit(TaskLoaded(dummyTasks));
    });
    on<FilterTasks>((event, emit) {
      // Handle the filter tasks logic here
      if (state is TaskLoaded) {
        emit(const TaskLoading());
        if (event.status == 'All') {
          emit(TaskLoaded(dummyTasks));
        } else if (event.status == 'Recent Task') {
          final filtered = dummyTasks
              .where((t) => t.status == 'Ongoing')
              .toList();
          emit(TaskLoaded(filtered));
        }
      }
    });
  }
}
