class EditUserPasswordEntity {
  final String oldPassword;
  final String newPassword;
  final String confirmPassword;

  const EditUserPasswordEntity({
    required this.oldPassword,
    required this.newPassword,
    required this.confirmPassword,
  });
}
