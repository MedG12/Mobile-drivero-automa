class EditPasswordResponse {
  final String status;
  final EditPasswordResult? result;

  EditPasswordResponse({
    required this.status,
    this.result,
  });

  factory EditPasswordResponse.fromJson(Map<String, dynamic> json) {
    return EditPasswordResponse(
      status: json["status"] ?? "",
      result: json["result"] != null
          ? EditPasswordResult.fromJson(json["result"])
          : null,
    );
  }
}

class EditPasswordResult {
  final String message;

  EditPasswordResult({
    required this.message,
  });

  factory EditPasswordResult.fromJson(Map<String, dynamic> json) {
    return EditPasswordResult(
      message: json["message"] ?? "",
    );
  }
}
