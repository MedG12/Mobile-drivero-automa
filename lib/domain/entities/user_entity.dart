class UserEntity {
  final int userId;
  final String name;
  final String email;
  final String? photoUrl;
  final int? gender; 
  final String app;
  final String token;
  final int expires;
  final DateTime? birthDate;
  

  UserEntity({
    required this.userId,
    required this.name,
    required this.email,
    required this.app,
    required this.token,
    required this.expires,
    this.photoUrl,
    this.gender, 
    this.birthDate,
  });

 
  }

