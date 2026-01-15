import 'package:drivero_automa/data/models/user_model.dart';

class EditUserBiodataResponse {
  final String? status;
  final UserModel? result;

  EditUserBiodataResponse({this.status, this.result});

  factory EditUserBiodataResponse.fromJson(Map<String, dynamic> json) {
    return EditUserBiodataResponse(
      status: json["status"],
      result: json["result"] != null
          ? UserModel.fromJson(json["result"])
          : null,
    );
  }
}
