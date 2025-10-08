import 'package:drivero_automa/config/app_config.dart';
import 'package:drivero_automa/core/widgets/background_page.dart';
import 'package:drivero_automa/features/profile/presentation/widgets/customTextField.dart';
import 'package:drivero_automa/features/profile/presentation/widgets/customDropdownField.dart';
import 'package:drivero_automa/gen/assets.gen.dart';
import 'package:drivero_automa/theme/text_theme.dart';
import 'package:drivero_automa/theme/theme.dart';
import 'package:drivero_automa/utils/date/date_format_utils.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class EditProfilePage extends StatefulWidget {
  const EditProfilePage({super.key});

  @override
  State<EditProfilePage> createState() => _EditProfilePageState();
}

class _EditProfilePageState extends State<EditProfilePage> {
  final TextEditingController nameController = TextEditingController(
    text: "Beji Driver",
  );
  final TextEditingController emailController = TextEditingController(
    text: "bejidriver@gmail.com",
  );
  final TextEditingController passwordController = TextEditingController(
    text: "********",
  );
  final TextEditingController plateNumberController = TextEditingController(
    text: "AD1234 BCF",
  );
  final TextEditingController brandController = TextEditingController(
    text: "Tesla Model X",
  );

  String gender = "Male";
  String vehicleType = "Car";
  DateTime? dob = DateTime(1987, 7, 7);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: BackgroundPageWidget(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              SizedBox(
                height: 56, // tinggi header
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    // Title di tengah
                    Center(
                      child: Text(
                        "Edit Profile",
                        style: AppTextTheme.headline3.copyWith(
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),

                    // Tombol back di kiri
                    Positioned(
                      left: 0,
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          padding: EdgeInsets.zero,
                          minimumSize: const Size(46, 36),
                          backgroundColor: Colors.white,
                          shape: const RoundedRectangleBorder(
                            borderRadius: BorderRadius.all(Radius.circular(50)),
                          ),
                        ),
                        onPressed: () {
                          context.pop();
                        },
                        child: Icon(
                          Icons.arrow_back,
                          color: MyTheme.color.primary,
                          size: 24,
                        ),
                      ),
                    ),
                  ],
                ),
              ),

              // Profile Picture
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

              // Personal
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
              CustomTextField(label: "Name", controller: nameController),

              const SizedBox(height: 12),

              Row(
                children: [
                  Expanded(
                    child: CustomDropdown(
                      label: "Gender",
                      items: ["Male", "Female"],
                      value: gender,
                      onChanged: (val) => setState(() => gender = val!),
                      iconBuilder: (val) {
                        switch (val) {
                          case "Male":
                            return Icons.male;
                          case "Female":
                            return Icons.female;
                          default:
                            return Icons.arrow_drop_down;
                        }
                      },
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Date of Birth",
                          style: AppTextTheme.headline5.copyWith(
                            color: Colors.white,
                          ),
                        ),
                        SizedBox(height: AppSetting.setHeight(8)),
                        InkWell(
                          onTap: () async {
                            final picked = await showDatePicker(
                              context: context,
                              initialDate: dob ?? DateTime(2000),
                              firstDate: DateTime(1900),
                              lastDate: DateTime.now(),
                            );
                            if (picked != null) setState(() => dob = picked);
                          },
                          child: InputDecorator(
                            decoration: InputDecoration(
                              isDense: true,
                              contentPadding: const EdgeInsets.symmetric(
                                horizontal: 16,
                                vertical: 6,
                              ),
                              filled: true,
                              fillColor: Colors.white,
                              border: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(12),
                                borderSide: BorderSide.none,
                              ),
                              suffixIcon: const Icon(
                                Icons.calendar_today,
                                color: Colors.blue,
                              ),
                            ),
                            child: Text(
                              dob != null
                                  ? formatDate.format(dob!)
                                  : "Select Date",
                              style: const TextStyle(color: Colors.black87),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 24),
              SizedBox(height: AppSetting.setHeight(24)),
              // Email & Password
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  "Email & Password",
                  style: AppTextTheme.headline4.copyWith(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              SizedBox(height: AppSetting.setHeight(24)),
              CustomTextField(controller: emailController),
              CustomTextField(
                controller: passwordController,
                icon: const Icon(Icons.visibility_off),
              ),
              SizedBox(height: AppSetting.setHeight(24)),
              // Vehicle Detail
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  "Vehicle Detail",
                  style: AppTextTheme.headline4.copyWith(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              SizedBox(height: AppSetting.setHeight(24)),
              Row(
                children: [
                  Expanded(
                    child: CustomDropdown(
                      label: "Vehicle Type",
                      items: ["Car", "Motorcycle"],
                      value: vehicleType,
                      onChanged: (val) => setState(() => vehicleType = val!),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: CustomTextField(
                      label: "Plate Number",
                      controller: plateNumberController,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              CustomTextField(label: "Brand", controller: brandController),
              const SizedBox(height: 12),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  onPressed: () {},
                  child: Text(
                    "Save",
                    style: AppTextTheme.bodyText1.copyWith(
                      color: MyTheme.color.primary,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
