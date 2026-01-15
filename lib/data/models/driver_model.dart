import 'package:drivero_automa/domain/entities/driver_entity.dart';

class DriverModel extends DriverEntity {
  final int driverId;
  final int userId;
  final String? name;
  final String? telp;
  final String? ktp;
  final DateTime? birthDate;
  final int? gender;

  DriverModel({
    required this.driverId,
    required this.userId,
    this.name,
    this.telp,
    this.ktp,
    this.birthDate,
    this.gender,
  }) : super(
         driverId: driverId,
         userId: userId,
         name: name,
         telp: telp,
         ktp: ktp,
         birthDate: birthDate,
         gender: gender,
       );

  factory DriverModel.fromJson(Map<String, dynamic> json) {
    return DriverModel(
      driverId: json['id'],
      userId: json['user_id'],
      name: json['name'],
      telp: json['telp'],
      ktp: json['ktp'],
      birthDate: json['birth_date'] != null
          ? DateTime.parse(json['birth_date'])
          : null,
      gender: json['gender'],
    );
  }
}
