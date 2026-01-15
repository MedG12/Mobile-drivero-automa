import 'package:drivero_automa/domain/entities/user_entity.dart';

class UserModel extends UserEntity {
  UserModel({
    required int userId,
    required String name,
    required String email,
    required String app,
    required String token,
    required int expires,
    String? photoUrl,
    DateTime? birthDate,
    int? gender,
  }) : super(
         userId: userId,
         name: name,
         email: email,
         app: app,
         token: token,
         expires: expires,
         photoUrl: photoUrl,
         birthDate: birthDate,
         gender: gender,
       );

  /// ✅ Parsing dari API (login / get profile)
  factory UserModel.fromJson(Map<String, dynamic> json) {
    // 🔹 CRITICAL FIX: Parse birth_date tanpa timezone conversion
    DateTime? parsedBirthDate;
    if (json['birth_date'] != null) {
      try {
        final dateStr = json['birth_date'].toString();

        // Ambil hanya bagian tanggal (ignore waktu dan timezone)
        // Format bisa: "2024-12-14" atau "2024-12-14T00:00:00.000Z"
        final datePart = dateStr.split('T')[0].split(' ')[0];
        final parts = datePart.split('-');

        if (parts.length == 3) {
          parsedBirthDate = DateTime(
            int.parse(parts[0]), // year
            int.parse(parts[1]), // month
            int.parse(parts[2]), // day
          );
        }
      } catch (e) {
        print('❌ Error parsing birth_date: $e from ${json['birth_date']}');
      }
    }

    return UserModel(
      userId: json['user_id'] ?? 0,
      name: json['name'] ?? '',
      email: json['email'] ?? '',
      app: json['app'] ?? '',
      token: json['token'] ?? '',
      expires: json['expires'] ?? 0,
      photoUrl: json['photo_url'],
      birthDate: parsedBirthDate,
      gender: json['gender'],
    );
  }

  /// ✅ copyWith untuk update sebagian (edit biodata)
  UserModel copyWith({
    String? name,
    DateTime? birthDate,
    int? gender,
    String? email,
    String? app,
    String? token,
    int? expires,
    String? photoUrl,
  }) {
    return UserModel(
      userId: userId,
      name: name ?? this.name,
      email: email ?? this.email,
      app: app ?? this.app,
      token: token ?? this.token,
      expires: expires ?? this.expires,
      photoUrl: photoUrl ?? this.photoUrl,
      birthDate: birthDate ?? this.birthDate,
      gender: gender ?? this.gender,
    );
  }

  /// ✅ Simpan ke local (SharedPreferences)
  Map<String, dynamic> toJson() {
    // 🔹 CRITICAL FIX: Format birth_date tanpa timezone
    String? formattedBirthDate;
    if (birthDate != null) {
      formattedBirthDate =
          '${birthDate!.year}-'
          '${birthDate!.month.toString().padLeft(2, '0')}-'
          '${birthDate!.day.toString().padLeft(2, '0')}';
    }

    return {
      'user_id': userId,
      'name': name,
      'email': email,
      'app': app,
      'token': token,
      'expires': expires,
      'photo_url': photoUrl,
      'birth_date': formattedBirthDate, // 🔹 Format: "2024-12-14"
      'gender': gender,
    };
  }

  /// ✅ Helper kalau response list
  static List<UserModel> listFromJson(List<dynamic> list) {
    return list.map((e) => UserModel.fromJson(e)).toList();
  }
}
