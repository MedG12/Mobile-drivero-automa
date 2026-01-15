import 'package:equatable/equatable.dart';

class TaskItem extends Equatable {
  final String title;
  final DateTime date;
  final String description;
  final String car;
  final String status;

  const TaskItem({
    required this.title,
    required this.date,
    required this.description,
    required this.car,
    required this.status,
  });
  @override
  List<Object?> get props => [title, date, description, car, status];
}
