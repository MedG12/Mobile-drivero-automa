import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/domain/entities/user_entity.dart';
import 'package:drivero_automa/domain/entities/edit_user_biodata_etity.dart';
import 'package:drivero_automa/domain/entities/edit_user_password_etity.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_bloc.dart';
import 'package:drivero_automa/presentation/auth/bloc/auth_event.dart';
import 'package:drivero_automa/presentation/profile/bloc/profile_bloc.dart';
import 'package:drivero_automa/presentation/profile/bloc/profile_event.dart';
import 'package:drivero_automa/presentation/profile/bloc/profile_state.dart';
import 'package:drivero_automa/presentation/profile/widgets/customTextField.dart';
import 'package:drivero_automa/presentation/profile/widgets/customDropdownField.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';

class EditProfilePage extends StatefulWidget {
  const EditProfilePage({super.key});

  @override
  State<EditProfilePage> createState() => _EditProfilePageState();
}

class _EditProfilePageState extends State<EditProfilePage> {
  final TextEditingController nameController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController oldPasswordController = TextEditingController();
  final TextEditingController newPasswordController = TextEditingController();
  final TextEditingController confirmPasswordController =
      TextEditingController();

  DateTime? dob;
  String? originalName;
  int? originalGender;
  bool isChanged = false;
  int gender = 1; // 1=Male, 2=Female

  @override
  void initState() {
    super.initState();
    context.read<ProfileBloc>().add(GetProfileEvent());

    // Listener untuk perubahan field
    nameController.addListener(_onChanged);
    oldPasswordController.addListener(_onChanged);
    newPasswordController.addListener(_onChanged);
    confirmPasswordController.addListener(_onChanged);
  }

  // 🔹 Selalu update field dari user terbaru
  void _initializeFromUser(UserEntity user) {
    nameController.text = user.name;
    emailController.text = user.email;
    dob = user.birthDate;
    gender = user.gender ?? 1; // Ambil dari backend
    originalName = user.name;
    originalGender = gender;
    setState(() {});
  }

  void _onChanged() {
    setState(() {
      isChanged =
          nameController.text != (originalName ?? '') ||
          oldPasswordController.text.isNotEmpty ||
          newPasswordController.text.isNotEmpty ||
          confirmPasswordController.text.isNotEmpty ||
          gender != (originalGender ?? gender) ||
          dob != null;
    });
  }

  @override
  void dispose() {
    nameController.dispose();
    emailController.dispose();
    oldPasswordController.dispose();
    newPasswordController.dispose();
    confirmPasswordController.dispose();
    super.dispose();
  }

  void _saveChanges(UserEntity user) {
    final String currentName = nameController.text;
    final int currentGender = gender;
    final String currentDateBirth = dob != null
        ? DateFormat('yyyy-MM-dd').format(dob!)
        : '';

    if (currentName != (originalName ?? '') ||
        currentGender != (originalGender ?? gender) ||
        currentDateBirth !=
            (user.birthDate != null
                ? DateFormat('yyyy-MM-dd').format(user.birthDate!)
                : '')) {
      final updatedUser = UserEntity(
        name: currentName,
        gender: currentGender,
        birthDate: dob,
        userId: user.userId,
        email: user.email,
        app: user.app,
        token: user.token,
        expires: user.expires,
      );
      context.read<ProfileBloc>().add(UpdateBiodataEvent(updatedUser));
      context.read<AuthBloc>().add(UpdateAuthUserEvent(updatedUser));
    }

    // Change password jika ada input
    if (oldPasswordController.text.isNotEmpty &&
        newPasswordController.text.isNotEmpty &&
        confirmPasswordController.text.isNotEmpty) {
      if (newPasswordController.text == confirmPasswordController.text) {
        context.read<ProfileBloc>().add(
          ChangePasswordEvent(
            EditUserPasswordEntity(
              oldPassword: oldPasswordController.text,
              newPassword: newPasswordController.text,
              confirmPassword: confirmPasswordController.text,
            ),
          ),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text("New password & confirm password do not match"),
            backgroundColor: Colors.red,
          ),
        );
        return;
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: BackgroundPageWidget(
        child: BlocConsumer<ProfileBloc, ProfileState>(
          listener: (context, state) {
            if (state is ProfileLoaded) {
              _initializeFromUser(state.user); // 🔹 selalu update field
            } else if (state is UpdateBiodataSuccess ||
                state is ChangePasswordSuccess) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(
                    state is UpdateBiodataSuccess
                        ? 'Profile updated successfully!'
                        : 'Password changed successfully!',
                  ),
                ),
              );

              // Clear password field & reset change state
              oldPasswordController.clear();
              newPasswordController.clear();
              confirmPasswordController.clear();
              setState(() => isChanged = false);

              // Refresh user profile
              context.read<ProfileBloc>().add(GetProfileEvent());
            } else if (state is UpdateBiodataError ||
                state is ChangePasswordError) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(
                    'Error: ${state is UpdateBiodataError ? state.message : (state as ChangePasswordError).message}',
                  ),
                  backgroundColor: Colors.red,
                ),
              );
            }
          },
          builder: (context, state) {
            if (state is ProfileLoading) {
              return const Center(
                child: CircularProgressIndicator(color: Colors.white),
              );
            }

            if (state is ProfileError) {
              return Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      'Error: ${state.message}',
                      style: const TextStyle(color: Colors.white),
                    ),
                    const SizedBox(height: 16),
                    ElevatedButton(
                      onPressed: () =>
                          context.read<ProfileBloc>().add(GetProfileEvent()),
                      child: const Text('Retry'),
                    ),
                  ],
                ),
              );
            }

            if (state is! ProfileLoaded) return const SizedBox.shrink();
            final user = state.user;

            return SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                children: [
                  // HEADER
                  SizedBox(
                    height: 56,
                    child: Stack(
                      alignment: Alignment.center,
                      children: [
                        Center(
                          child: Text(
                            "Edit Profile",
                            style: AppTextTheme.headline3.copyWith(
                              color: Colors.white,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                        Positioned(
                          left: 0,
                          child: IconButton(
                            onPressed: () => context.pop(),
                            icon: const Icon(
                              Icons.arrow_back,
                              color: Colors.white,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 24),

                  // FOTO PROFIL
                  Stack(
                    alignment: Alignment.bottomRight,
                    children: [
                      CircleAvatar(
                        radius: 50,
                        backgroundImage: AssetImage(Assets.images.ppUser.path),
                      ),
                      CircleAvatar(
                        radius: 18,
                        backgroundColor: Colors.white,
                        child: Icon(Icons.camera_alt, color: Colors.blue[900]),
                      ),
                    ],
                  ),
                  const SizedBox(height: 24),

                  // PERSONAL
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text(
                      "Personal",
                      style: AppTextTheme.headline4.copyWith(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  const SizedBox(height: 24),

                  // NAME
                  CustomTextField(label: "Name", controller: nameController),
                  const SizedBox(height: 12),

                  Row(
                    children: [
                      Expanded(
                        child: CustomDropdown(
                          label: "Gender",
                          items: const ["Male", "Female"],
                          value: gender == 1 ? "Male" : "Female",
                          onChanged: (val) {
                            setState(() {
                              gender = val == "Male" ? 1 : 2;
                              _onChanged();
                            });
                          },
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: CustomTextField(
                          initialValue: dob != null
                              ? DateFormat('yyyy-MM-dd').format(dob!)
                              : '',
                          label: "Date of Birth",
                          readOnly: true,
                          icon: Icon(
                            Icons.calendar_month,
                            color: AppTheme.lightTheme.primaryColor,
                            size: 22,
                          ),
                          onTap: () async {
                            DateTime? picked = await showDatePicker(
                              context: context,
                              currentDate: dob,
                              initialDate: dob ?? DateTime(2000),
                              firstDate: DateTime(1900),
                              lastDate: DateTime.now(),
                            );
                            if (picked != null) {
                              setState(() {
                                dob = DateTime(
                                  picked.year,
                                  picked.month,
                                  picked.day,
                                  12,0,0
                                );
                                _onChanged();
                              });
                            }
                          },
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 24),

                  // CHANGE PASSWORD
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text(
                      "Change Password",
                      style: AppTextTheme.headline4.copyWith(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  const SizedBox(height: 24),
                  CustomTextField(
                    controller: oldPasswordController,
                    label: "Old Password",
                  ),
                  CustomTextField(
                    controller: newPasswordController,
                    label: "New Password",
                  ),
                  CustomTextField(
                    controller: confirmPasswordController,
                    label: "Confirm Password",
                  ),
                  const SizedBox(height: 24),

                  // BUTTON SAVE
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: isChanged
                            ? AppTheme.lightTheme.primaryColor
                            : Colors.grey[400],
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                        ),
                      ),
                      onPressed: isChanged ? () => _saveChanges(user) : null,
                      child: Text(
                        "Save",
                        style: AppTextTheme.bodyText1.copyWith(
                          color: MyTheme.color.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }
}
