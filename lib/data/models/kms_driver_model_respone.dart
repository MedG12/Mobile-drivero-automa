import 'package:drivero_automa/domain/entities/kms_driver_entity.dart';

class KmsDriverResponseModel {
  final int totalKms;

  KmsDriverResponseModel({required this.totalKms});

  factory KmsDriverResponseModel.fromJson(Map<String, dynamic> json) {
    return KmsDriverResponseModel(
      totalKms: json['result']?['total_kms'] ?? 0,
    );
  }

  KmsDriverEntity toEntity() {
    return KmsDriverEntity(
      totalKms: totalKms,
    );
  }
}
