class EditUserBiodataEntity {
  final String name;
  final String birth_date; // format: yyyy-mm-dd
  final int gender; // 1=Male, 2=Female

  const EditUserBiodataEntity({
    required this.name,
    required this.birth_date,
    required this.gender,
  });
  
  Map<String, dynamic> toJson() => {
        "name": name,
        "gender": gender,
        "birth_date": birth_date, // sesuai API
      };
}
