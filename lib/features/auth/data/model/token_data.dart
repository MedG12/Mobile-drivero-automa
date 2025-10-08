class TokenData {
  final String token;
  final DateTime expires;

  TokenData({required this.token, required this.expires});
  factory TokenData.fromJson(Map<String, dynamic> json) {
    return TokenData(
      token: json['token'] as String,
      expires: DateTime.fromMicrosecondsSinceEpoch(json['expires']),
    );
  }
}
