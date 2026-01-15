

import 'package:drivero_automa/data/models/flet_img_model.dart';
class FleetImageResponse {
  final List<FleetImageModel> result; // ubah dari data → result

  FleetImageResponse({required this.result});

  factory FleetImageResponse.fromJson(Map<String, dynamic> json) {
    final list = (json['result'] as List)
        .map((e) => FleetImageModel.fromJson(e))
        .toList();
    return FleetImageResponse(result: list);
  }

  Map<String, dynamic> toJson() {
    return {
      'result': result.map((e) => e.toJson()).toList(),
    };
  }
}
