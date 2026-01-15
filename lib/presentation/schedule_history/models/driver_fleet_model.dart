import 'package:equatable/equatable.dart';
class DriverFleetModel extends Equatable {
  final int id;
  final String name;

  DriverFleetModel({required this.id, required this.name});

  @override
  List<Object?> get props => [id, name];
}